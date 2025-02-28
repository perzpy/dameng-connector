# Dameng Connector

基于 Debezium 和 Flink CDC 的达梦数据库变更数据捕获解决方案

## 项目简介

Dameng Connector 是一个专门为达梦数据库(DM Database)设计的变更数据捕获(CDC)解决方案。该项目通过扩展 Debezium 和 Flink CDC，实现了对达梦数据库的实时数据变更监控、捕获和处理能力，为数据集成、数据同步、实时分析等场景提供强大支持。

## 快速开始

### 前置条件

- Java 8+
- 达梦数据库 8.x+
- Maven 3.6+

### 安装配置

1. **克隆仓库**

```bash
git clone https://github.com/yourusername/dameng-connector.git
cd dameng-connector
```

2. **编译项目**

```bash
./mvnw clean package -DskipTests
```

### 使用示例

#### 1. 独立模式运行 Debezium

```bash
# 启动 Debezium 独立服务器
debezium-server/run.sh

# 注册达梦连接器
curl -X POST -H "Content-Type: application/json" --data @dm-connector.json http://localhost:8083/connectors
```

## 配置选项

### Debezium 连接器配置

| 选项 | 描述 | 默认值 | 必填 |
|------|------|--------|------|
| connector.class | 连接器类名 | io.debezium.connector.dm.DMConnector | 是 |
| database.hostname | 达梦数据库主机名 | - | 是 |
| database.port | 达梦数据库端口号 | 5236 | 是 |
| database.user | 数据库用户名 | - | 是 |
| database.password | 数据库密码 | - | 是 |
| database.dbname | 数据库名称 | - | 是 |
| database.server.name | 连接器唯一名称 | - | 是 |
| table.include.list | 需要捕获的表列表 | - | 否 |
| snapshot.mode | 快照模式 | initial | 否 |
| transforms | 转换配置 | - | 否 |

## 版本兼容性

| 本项目版本 | Debezium 版本 | 达梦数据库版本 |
|-------|-------------|---------|
| 1.0.x | 1.5.x       | 8.x     |

## 贡献指南

我们非常欢迎社区贡献！如果您想参与项目开发，请遵循以下步骤：

1. Fork 本仓库
2. 创建您的特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交您的更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 开启一个 Pull Request

## 许可证

本项目采用 MIT 许可证。详情请参阅 [LICENSE](LICENSE) 文件。

## 联系方式

- 项目维护者：您的姓名
- 电子邮件：support@devlive.org
- 项目问题跟踪：[GitHub Issues](https://github.com/yourusername/dameng-connector/issues)

---

*备注：本项目不是达梦数据库官方项目，由社区维护和开发。*