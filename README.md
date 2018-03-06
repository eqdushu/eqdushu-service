Eqdushu Service
=============
EQ读书后端接口服务

Project Build
=========
mvn clean package -Dmaven.test.skip=true -Plocal

Project Deploy
=========
1.scp *.war file to remote tomcat server
2.use supervisor to manage start/stop tomcat process

Api Format
=========
http://*.eqdushu.com/eqdushu-service/*
