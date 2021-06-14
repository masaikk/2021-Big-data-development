# spkweek2

#### 介绍
pyspark sql 连接 hive

#### 软件架构
前后端分离的hive数据库连接系统
前端的代码位于studyelement文件夹中
后端的代码位于back文件夹中
系统的文档和展示的ppt位于pass文件夹中

在使用之前请把sql dependence等文件夹中的jar放入back/st1/static/tlib文件夹中，以使用jdbc

#### 环境要求
windows10 中文家庭版
node版本建议大于14并且使用npm作为包管理器。
anaconda3

#### 安装与使用教程

1.  分别开启后端和前端
2.  在back文件夹中安装python所需环境，确保jar包已经在指定文件夹中，shell运行python manage.py runserver，确保运行在本地8000端口
3.  在studyelement文件夹中运行npm install安装所需要的js包，运行npm run dev或者npm run serve以运行程序（会报一个webpack警告），确保运行在本地8080端口
4.  浏览器打开http://localhost:8080   http://localhost:8080/data  http://localhost:8080/tables 来查看结果 

#### 简要说明
/路径是用来设置hive数据库配置的。
/tables来显示表。
/data可以执行sql语句，展示表的名字和字段。

