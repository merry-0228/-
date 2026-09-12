# 宠物服务预约管理系统

这是一个面向对象的宠物服务预约管理系统，实现了多用户不同权限的功能，符合课程综合实验的要求。

## 项目结构
```
pet_service_system/
├── src/
│   └── com/
│       └── pet/
│           ├── entity/      # 实体类
│           │   ├── User.java
│           │   ├── Pet.java
│           │   ├── Shop.java
│           │   ├── ServiceItem.java
│           │   ├── Appointment.java
│           │   ├── Evaluation.java
│           │   └── Complaint.java
│           ├── dao/         # 数据访问层
│           │   ├── UserDao.java
│           │   ├── PetDao.java
│           │   ├── ShopDao.java
│           │   ├── ServiceItemDao.java
│           │   ├── AppointmentDao.java
│           │   ├── EvaluationDao.java
│           │   └── ComplaintDao.java
│           ├── util/        # 工具类
│           │   └── DBUtil.java
│           └── ui/          # 界面层
│               ├── LoginFrame.java
│               ├── RegisterFrame.java
│               ├── AdminMainFrame.java
│               ├── ShopMainFrame.java
│               └── UserMainFrame.java
├── lib/                    # 依赖包，放入mysql驱动
├── db/                     # 数据库相关
└── README.md
```

## 环境要求
1. Java 8+
2. MySQL 5.7/8.0
3. MySQL Connector/J 驱动（放入lib目录）

## 运行说明
1. **配置数据库**：
   - 启动本地MySQL服务
   - 修改 `src/com/pet/util/DBUtil.java` 中的数据库连接配置，修改为你自己的MySQL用户名和密码
   - 第一次运行程序时，会自动创建数据库和表，同时创建默认管理员账号

2. **编译运行**：
   - 下载MySQL Connector/J驱动，放入`lib`目录
   - 编译：
     ```bash
     javac -cp lib/mysql-connector-java-8.0.30.jar src/com/pet/**/*.java -d bin
     ```
   - 运行：
     ```bash
     java -cp bin:lib/mysql-connector-java-8.0.30.jar com.pet.ui.LoginFrame
     ```

3. **测试账号**：
   - 默认管理员账号：手机号`admin`，密码`admin123`
   - 你也可以自行注册账号，选择普通用户、VIP用户或者商家身份

## 功能说明
### 管理员权限
- 管理所有用户、店铺、服务、预约
- 处理用户投诉
- 查看系统营收统计

### 商家权限
- 管理自己的店铺信息
- 发布和管理服务项目
- 处理用户预约（接单、完成服务）
- 查看店铺评价和自己的收入

### 用户权限
- 浏览店铺和服务，预约服务
- 管理自己的宠物档案
- 查看自己的预约，进行支付、评价、取消预约
- 提交投诉

### VIP用户特权
- 所有服务享9折优惠
- 优先预约（系统内已实现价格折扣，优先预约功能已预留接口）

## 注意事项
- 请确保本地MySQL服务已启动，并且连接配置正确
- 第一次运行时会自动初始化数据库，无需手动创建
- 不同角色登录后会进入不同的操作界面，权限完全隔离
