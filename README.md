# RemindYou 健康提醒系统

一个基于 Java 的健康提醒系统，支持灵活的提醒频率配置、缓存机制、规则引擎等功能，适用于如用药提醒、健康打卡、周期性通知等场景。

## 🧠 项目特性

- ✅ 多种提醒频率支持（按天、按周、按月等）
- 🔁 支持规则过期时间和单位定义（分钟、小时、天、周、月）
- ⚙️ 枚举驱动设计，支持高度扩展的提醒规则配置
- 🗃️ 内置 Java 缓存模块，提升系统响应速度
- 📦 统一返回结构体（R<T>），便于前后端联调
- 🧪 结构清晰，易于维护和测试
- 📄 提供 Swagger UI 接口文档，便于接口调试和使用
- 🏷️ 支持微信小程序提醒，邮箱提醒

## 📁 项目结构

```
main/
├── java/com/health/remind/
│   ├── RemindYouApplication.java   # Spring Boot 启动类
│   ├── common/                     # 常量、缓存、枚举等通用模块
│   ├── config/                     # 全局配置和基础类
│   └── Generator.java              # mybatis 代码生成器
```

## 🚀 快速开始

### 环境要求

- JDK 17 或以上
- Maven 3.6+
- Redis
- MySQL 8.0+
- IDE 推荐 IntelliJ IDEA

### 启动方式

```bash
# 克隆项目
git clone https://github.com/your-username/remindyou.git
cd remindyou

# 使用 IDE 启动 RemindYouApplication.java 即可
```

## 🧩 关键模块说明

| 模块                    | 描述              |
|-----------------------|-----------------|
| `FrequencyEnum`       | 提醒频率类型（如每天、每周）  |
| `RuleExpiredTypeEnum` | 规则失效类型（如提前提醒）   |
| `JavaCache`           | 简单的本地缓存实现       |
| `RedisKeys`           | Redis key 管理工具类 |
| `StrategyContext`     | 策略上下文           |
| `ScheduledBase`       | 线程基础文件          |

