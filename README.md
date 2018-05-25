# DSMS

> 网络电子看板管理系统

* [DSMS_FC](./dsms_fc/README.md) : 前端项目
* [DSMS_FE](./DSMS_FE/README.md) : 后端项目

## Build Setup

``` bash
# install nginx
sudo apt-get install nginx

# install mysql
sudo apt-get install mysql-server
root password:toor

# install redis
sudo apt-get install redis-server
修改密码为abc123

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
sudo cp ../script/dsms.production.conf /etc/nginx/conf.d/dsms.conf
sudo service nginx reload

# 后端
cd DSMS_FE
mvn clean install
%TOMCAT_HOME%/bin/startup.sh
