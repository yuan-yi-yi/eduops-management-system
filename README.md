# EduOps 教培运营管理系统

> 基于 Vue3 + SpringBoot + Mybatis 的前后端分离后台管理系统，支持 Docker Compose 一键部署，已上线公网

公网地址：https://www.eduops2026.top    
测试账号
用户名：袁帅
密码：  12345678

## 一、项目简介
EduOps 教培运营管理系统 面向中小型教培机构，涵盖教培部门、员工、班级、学员管理、权限控制、操作日志、数据可视化、阿里云OSS文件上传等核心功能。
采用主流前后端分离架构，代码规范。

## 二、技术栈
### 前端
- Vue3 + 组合式API
- Pinia 状态仓库管理
- VueRouter路由配置
- ElementPlus组件库
- ECharts数据可视化
- Vite构建工具

### 后端
- SpringBoot核心框架、DIO容器管理和IO依赖注入
- MyBatis操作数据库
- MySQL 8
- JWT令牌登录认证
- AOP切面日志
- 阿里云OSS

### 运维部署
- Docker容器化部署
- Docker Compose 一键部署
- Nginx方向代理技术和https配置
- Linux 云服务器

## 三、核心功能
- 登录认证 + JWT 令牌校验
- 部门管理、员工管理
- 班级管理、学员管理
- 运营数据可视化
- 阿里云 OSS 文件上传
- 全局异常处理 + 操作日志记录

## 四、项目目录结构
eduops-management-system/
├── backend/ # SpringBoot 后端源码
├── frontend/ # Vue3 前端源码
├── database/ # 数据库建表 SQL
└── README.md # 项目说明

## 五、项目亮点
完整前后端分离业务开发，模块划分清晰
集成 JWT + AOP 实现权限与日志管控
接入阿里云 OSS 实现文件云端存储
支持 Docker Compose 一行命令全自动部署
已配置域名上线可直接访问

