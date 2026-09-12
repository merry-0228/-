基于Java Swing + MySQL的宠物服务预约管理系统（多角色权限/三层架构/RBAC/状态机）。

宠物服务预约管理系统 (Pet Service Appointment System)
https://img.shields.io/badge/Java-JDK%25201.8-blue
https://img.shields.io/badge/MySQL-8.0-orange
https://img.shields.io/badge/GUI-Java%2520Swing-lightgrey
https://img.shields.io/badge/Architecture-Three--Tier-green

项目简介
针对传统宠物门店纸质登记效率低、数据难统计的痛点，本项目开发了一套基于 Java Swing 与 MySQL 的桌面端管理系统。系统采用经典的三层架构（实体层 - 数据访问层 - 界面层），实现了管理员、商家、普通用户、VIP用户四类角色的权限分离与业务闭环，覆盖店铺管理、服务发布、在线预约、订单支付、评价反馈、投诉处理及营收统计等核心功能。

项目定位：软件工程专业《面向对象程序设计》课程设计。项目从需求分析、E-R图设计、UI原型设计到编码测试实现了全流程交付。

技术栈
开发语言：Java (JDK 1.8)

图形界面：Java Swing (JFrame + JTabbedPane + JTable)

数据库：MySQL 8.0

数据访问：原生 JDBC (PreparedStatement 预编译防SQL注入)

架构模式：三层架构（Entity + DAO + UI）

工具链：IntelliJ IDEA、Navicat

核心功能模块
系统严格遵循 RBAC（基于角色的权限访问控制）模型，各角色权限隔离：

管理员端：用户管理、店铺管理、服务管理、预约管理、评价管理、投诉处理（含状态流转）、平台营收与VIP优惠统计。

商家端：店铺信息维护、服务项目管理（洗护/美容/寄养/医疗）、接单与完成服务（状态流转）、收入统计。

用户端（普通/VIP）：浏览店铺与服务、提交预约（VIP自动计算9折折扣）、宠物档案管理（增删改查）、订单管理（支付/取消/评价）、在线投诉。

公共模块：手机号唯一性校验、登录角色自动跳转、全局异常与参数校验。

数据库设计
项目遵循第三范式，共设计 7 张核心数据表（用户表、店铺表、服务项目表、预约表、评价表、宠物表、投诉表），通过外键约束保障参照完整性。包含完整的 E-R 图与关系模型。

项目目录结构
pet_service_system/
├── src/
│   └── com/pet/
│       ├── entity/      # 实体层：与数据库表一一对应
│       ├── dao/         # 数据访问层：JDBC 封装与增删改查
│       ├── util/        # 工具类：DBUtil（数据库连接与自动建表）
│       └── ui/          # 界面层：各角色 Swing 图形交互界面
├── lib/                 # 第三方依赖：MySQL Connector/J
├── db/                  # 数据库脚本与备份文件（pet_service.sql）
└── doc/                 # 项目文档（需求规格说明书、E-R图、测试用例）

快速开始
环境准备：确保本地已安装 JDK 1.8+ 及 MySQL 8.0。
导入数据库：将 db/pet_service.sql 导入 MySQL，或首次运行时由 DBUtil 自动建库建表。
配置数据库：在 com/pet/util/DBUtil.java 中修改你的数据库账号密码。
运行程序：使用 IDEA 打开项目，运行 LoginFrame.java 即可启动系统。
默认管理员账号：admin / 123456 
默认商家账号：shop / 123456
默认用户账号：user / 123456
