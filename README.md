# DSMS

> 网络电子看板管理系统

* [DSMS_FC](./dsms_fc/README.md) : 前端项目
* [DSMS_FE](./DSMS_FE/README.md) : 后端项目

## Build Setup

``` bash
# install nginx
sudo apt-get install nginx

# install mysql
sudo apt-get install mysql
root password:toor

# install maven

# install tomcat

# install nodejs

# install ffmpeg
sudo apt install ffmpeg
```

## 快速部署

``` bash
# 前端
cd DSMS_FC
npm install
npm run build

# 后端
cd DSMS_FE
mvn clean install
