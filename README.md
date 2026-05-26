# QimiMaid

![GitHub Actions Build](https://github.com/MC-shanshuo/SNA-QimiMaid/actions/workflows/build.yml/badge.svg)
![GitHub Release](https://img.shields.io/github/v/release/MC-shanshuo/SNA-QimiMaid)

QimiMaid 是一个为 Minecraft 服务器设计的扫地娘插件，提供自动清理掉落物和怪物的功能，并支持回收站功能。

## 功能特性

### 🗑️ 垃圾桶系统
- 玩家可以打开垃圾桶 GUI
- 快速丢弃不需要的物品
- 支持拖拽操作

### ♻️ 回收站系统
- 自动回收被清理的掉落物和生物掉落物
- 分类浏览回收物品
- 支持使用经济系统回购回收的物品

### 🧹 自动清理
- 自动清理地面掉落物
- 自动清理怪物实体
- 可配置清理间隔时间
- 支持白名单和黑名单设置

### 🏪 经济系统集成
- 集成 Vault 经济插件
- 回购物品需要消耗金币
- 可配置物品回购价格

## 安装要求

- Minecraft 服务端: Paper 1.21.x 或更高版本
- Vault 插件 (用于经济功能)
- 一个兼容 Vault 的经济插件 (如 EssentialsX)

## 安装方法

1. 下载最新版本的 QimiMaid.jar
2. 将 JAR 文件放入服务器的 `plugins` 目录
3. 启动服务器，插件会自动生成配置文件
4. 根据需要修改配置文件
5. 使用 `/reload` 命令重新加载插件

## 命令

| 命令 | 别名 | 描述 | 权限 |
|------|------|------|------|
| `/qimimaid` | `qm`, `maid` | 插件主命令 | `qimimaid.admin` |
| `/trashcan` | `tc`, `trash` | 打开垃圾桶 | `qimimaid.trashcan` |
| `/recycle` | `rc`, `recyclebin` | 打开回收站 | `qimimaid.recycle` |

## 权限

| 权限节点 | 描述 | 默认 |
|----------|------|------|
| `qimimaid.admin` | 允许使用管理员命令 | OP |
| `qimimaid.trashcan` | 允许使用垃圾桶功能 | 所有人 |
| `qimimaid.recycle` | 允许使用回收站功能 | 所有人 |
| `qimimaid.recycle.buy` | 允许购买回收站物品 | 所有人 |

## 配置文件

配置文件位于 `plugins/QimiMaid/config.yml`

```yaml
# 自动清理设置
clean:
  # 是否启用自动清理
  enabled: true
  # 清理间隔（秒）
  interval: 300
  # 最大掉落时间（秒）
  max_age: 600

# 回收站设置
recycle:
  # 是否启用回收站
  enabled: true
  # 物品保存时间（秒）
  save_duration: 86400
  # 默认回购价格倍率
  price_multiplier: 0.5

# 消息设置
messages:
  prefix: "[QimiMaid] "
  no_permission: "&c你没有权限执行此命令！"
  economy_not_found: "&c未找到经济插件！"
```

## 使用说明

### 使用垃圾桶

1. 输入 `/trashcan` 命令打开垃圾桶
2. 将物品拖入垃圾桶界面
3. 关闭界面后物品将被销毁

### 使用回收站

1. 输入 `/recycle` 命令打开回收站
2. 浏览分类查看回收的物品
3. 点击物品查看价格
4. 确认购买后物品将放入背包

## 开发

### 构建项目

```bash
mvn clean package -DskipTests
```

### 项目结构

```
src/main/
├── java/com/github/mcshanshuo/qimimaid/
│   ├── QimiMaid.java          # 主类
│   ├── commands/              # 命令处理
│   ├── listeners/             # 事件监听器
│   ├── managers/              # 管理器
│   ├── gui/                   # GUI 界面
│   ├── model/                 # 数据模型
│   └── utils/                 # 工具类
└── resources/
    ├── config.yml             # 配置文件
    ├── messages.yml           # 消息配置
    └── plugin.yml             # 插件描述
```

## 贡献

欢迎提交 Issue 和 Pull Request！

## 许可证

本项目使用 MIT 许可证。

## 联系方式

如有问题或建议，请在 GitHub 上提交 Issue。