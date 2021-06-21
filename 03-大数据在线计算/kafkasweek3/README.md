# kafkasweek3

#### 介绍
本软件系统是使用kafka数据流和数据库S3，将kafka数据流先转化成JSON形式，保存到S3数据库中。

#### 软件架构
目录如下，proj文件夹下是大作业的代码
homework文件夹下面是实训拓展题的代码
pass文件夹下面的是文档及ppt
图例1.uxf和图例2.uxf为流程图

#### 安装教程

1.  git clone 本仓库
2.  在homework和proj文件夹下面依次运行Main函数


#### 使用说明

如果module文件夹下面有pom.xml文件，需要先安装maven依赖。
在运行代码之前，先导入Scala依赖，版本为3.11。还需要导入flink dependencies依赖文件夹中的jar包。
本项目中使用的mysql数据库为自己云服务器的数据库
检查数据可以使用119.23.182.180为mysql的地址。
