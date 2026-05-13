# EduOps 教培运营管理系统

> 基于 Vue3 + SpringBoot 前后端分离后台管理系统，支持 Docker Compose 一键部署，已上线公网演示

公网演示地址：https://www.eduops2026.top

## 一、项目简介
EduOps 教培运营管理系统 面向中小型教培机构，涵盖部门、员工、班级、学员管理、权限控制、操作日志、数据可视化、阿里云OSS文件上传等核心功能。
采用主流前后端分离架构，代码规范，适合课程设计、毕设、求职简历项目展示。

## 二、技术栈
### 前端
- Vue3 + Composition API
- Pinia 状态管理
- Vue Router
- Element Plus
- ECharts 数据可视化
- Vite

### 后端
- SpringBoot
- MyBatis
- MySQL 8
- JWT 认证
- AOP 切面日志
- 阿里云 OSS

### 运维部署
- Docker
- Docker Compose
- Nginx
- Linux 云服务器

## 三、核心功能
- 登录认证 + JWT 令牌校验
- 基于角色 RBAC 权限控制
- 部门管理、员工管理
- 班级管理、学员管理
- 运营数据可视化大屏
- 阿里云 OSS 文件上传
- 全局异常处理 + 操作日志记录

## 四、项目目录结构
eduops-management-system/
├── backend/ # SpringBoot 后端源码
├── frontend/ # Vue3 前端源码
├── database/ # 数据库建表 SQL
├── docker-compose.yml # 一键部署编排文件
├── .env # 环境变量配置（本地保留，不上传开源）
└── README.md # 项目说明
plaintext

## 五、本地运行
### 1. 数据库
1. 创建 MySQL 数据库 `eduops_db`
2. 执行 `database/eduops_db_schema.sql`

### 2. 后端
修改 `application.yml` 数据库、OSS 为自己本地配置，启动主类即可。

### 3. 前端
```bash
cd frontend
npm install
npm run dev


六、Docker 一键部署
1. 部署命令
bash
运行
# 拉取项目
git clone https://github.com/yuan-yi-yi/eduops-management-system.git
cd eduops-management-system

# 一键启动所有服务（mysql + 后端 + 前端）
docker-compose up -d
2. 访问地址
在线演示：https://www.eduops2026.top
后端接口：http:// 服务器 IP:8080
3. 停止服务
bash
运行
docker-compose down



七、项目亮点
完整前后端分离业务开发，模块划分清晰
集成 JWT + AOP 实现权限与日志管控
接入阿里云 OSS 实现文件云端存储
支持 Docker Compose 一行命令全自动部署
已配置域名上线可直接访问，简历项目含金量高



八、相关地址
GitHub 项目：https://github.com/yuan-yi-yi/eduops-management-system
在线演示：https://www.eduops2026.top
测试账号
用户名：袁帅
密码：  12345678