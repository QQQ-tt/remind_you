@echo off
setlocal enabledelayedexpansion

:: 定义颜色代码（加粗）
set "COLOR_INFO=[1;34mINFO[0m"    :: 加粗蓝色
set "COLOR_ERROR=[1;31mERROR[0m"  :: 加粗红色
set "COLOR_WARN=[1;33mWARN[0m"    :: 加粗黄色

:: 服务器IP/域名
set DEPLOY_HOST=qqqtx.com
:: 服务器工作目录
set REMOTE_WORKDIR=/root/install
:: Maven构建命令
set MAVEN_GOAL=clean package -DskipTests
:: 项目名称
set PROJECT_NAME=remind_you
set ENV=prod

:: 生成动态版本号（格式：1.0.20240615-0843）
for /f "tokens=2 delims==" %%G in ('wmic os get localdatetime /value') do set datetime=%%G
set BUILD_TIMESTAMP=%datetime:~0,8%-%datetime:~8,4%
set VERSION=1.0.%BUILD_TIMESTAMP%

echo [%COLOR_INFO%] Building version %VERSION%

:: 阶段1：本地Maven打包
echo [%COLOR_INFO%] Maven packaging is being performed...
call mvn %MAVEN_GOAL%
if %ERRORLEVEL% neq 0 (
    echo [%COLOR_ERROR%] Maven build failed
    exit /b 1
)

:: 验证JAR文件生成
set JAR_FILE=target\%PROJECT_NAME%-*.jar
if not exist %JAR_FILE% (
    echo [%COLOR_ERROR%] The generated jar file was not found
    exit /b 1
)

:: 阶段2：准备传输文件
echo [%COLOR_INFO%] Deployment files are being prepared...
mkdir deploy_temp 2>nul
copy Dockerfile deploy_temp >nul
for %%f in (%JAR_FILE%) do set "JAR_PATH=%%f"
copy "%JAR_PATH%" deploy_temp\%PROJECT_NAME%.jar >nul

:: 阶段3：安全传输文件
echo [%COLOR_INFO%] Uploading deployment files to the server...
scp -r ./deploy_temp/* root@%DEPLOY_HOST%:/root/install/deploy_temp
if %ERRORLEVEL% neq 0 (
    echo [%COLOR_ERROR%] File transfer failed
    goto cleanup
)

:: 阶段4：远程Docker操作
echo [%COLOR_INFO%] Remote deployment is being performed...
ssh root@%DEPLOY_HOST% ^
    "cd %REMOTE_WORKDIR%/deploy_temp && "^
    "docker build -t remind:%VERSION% --build-arg JAR_FILE=%PROJECT_NAME%.jar . && "^
    "TAG=%VERSION% ENV=%ENV% docker compose -f %REMOTE_WORKDIR%/docker-compose.yaml up -d app && "^
    "rm -rf %REMOTE_WORKDIR%/deploy_temp"

if %ERRORLEVEL% neq 0 (
    echo [%COLOR_ERROR%] Remote deployment failed
    goto cleanup
)

:: 阶段5：清理文件
:cleanup
echo [%COLOR_INFO%] Temporary files are being cleaned up...
rmdir /s /q deploy_temp

echo [%COLOR_INFO%] The deployment was successful!
echo [%COLOR_INFO%] Version number: %VERSION%
