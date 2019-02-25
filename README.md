# JFinal-summary

#### 项目介绍
JFinal学习总结

#### 文件与文件夹说明

---

doc 存放配置与文档的文件夹.

|----conf 存放配置的文件夹,打包会根据不同环境拷贝指定环境文件夹中的配置文件.

       |----dev 内网环境

       |----test 集成环境

       |----release 正式环境

---

lib 项目用到的jar文件存放位置

---

pub 浏览器可直接访问到项目文件的文件夹

    |---- index.html 主页页面

---

src 项目JAVA源代码
    
    |---- server.sh 启动项目shell脚本

    |---- log4j2.xml log4j2日志配置文件

    |---- com.web.boot.Startup 项目启动CLASS

---

build_images.sh 镜像打包推送镜像库的脚本

---

DockerFile 生成镜像时候所用到的文件

---

webdefault.xml Jetty配置文件

---
