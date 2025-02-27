@echo off
setlocal enabledelayedexpansion

:: 服务器IP/域名
set DEPLOY_HOST=120.55.165.76
:: 服务器工作目录
set REMOTE_WORKDIR=/root/install
:: Maven构建命令
set MAVEN_GOAL=clean package -DskipTests
:: 项目名称
set PROJECT_NAME=remind_you

:: 生成动态版本号（格式：1.0.20240615-0843）
for /f "tokens=2 delims==" %%G in ('wmic os get localdatetime /value') do set datetime=%%G
set BUILD_TIMESTAMP=%datetime:~0,8%-%datetime:~8,4%
set VERSION=1.0.%BUILD_TIMESTAMP%

echo Building version %VERSION%

:: 阶段1：本地Maven打包
echo 正在执行Maven打包...
call mvn %MAVEN_GOAL%
if %ERRORLEVEL% neq 0 (
    echo [错误] Maven构建失败
    exit /b 1
)

:: 验证JAR文件生成
set JAR_FILE=target\%PROJECT_NAME%-*.jar
if not exist %JAR_FILE% (
    echo [错误] 未找到生成的JAR文件
    exit /b 1
)

:: 阶段2：准备传输文件
echo 正在准备部署文件...
mkdir deploy_temp 2>nul
copy Dockerfile deploy_temp >nul
for %%f in (%JAR_FILE%) do set "JAR_PATH=%%f"
copy "%JAR_PATH%" deploy_temp\%PROJECT_NAME%.jar >nul

:: 阶段3：安全传输文件
echo 正在上传部署文件到服务器...
scp -r ./deploy_temp/* root@%DEPLOY_HOST%:/root/install/deploy_temp
if %ERRORLEVEL% neq 0 (
    echo [错误] 文件传输失败
    goto cleanup
)

:: 阶段4：远程Docker操作
echo 正在执行远程部署...
ssh root@%DEPLOY_HOST% ^
    "cd %REMOTE_WORKDIR%/deploy_temp && "^
    "docker build -t remind:%VERSION% --build-arg JAR_FILE=%PROJECT_NAME%.jar . && "^
    "TAG=%VERSION% docker compose -f %REMOTE_WORKDIR%/docker-compose.yaml up -d app && "^
    "rm -rf %REMOTE_WORKDIR%/deploy_temp"

if %ERRORLEVEL% neq 0 (
    echo [错误] 远程部署失败
    goto cleanup
)

:cleanup
echo 正在清理临时文件...
rmdir /s /q deploy_temp

echo 部署成功!
echo 版本号: %VERSION%