#!/bin/sh

mvn clean package
bees app:deploy --appid seblm/xebia-name-learning --endPoint eu -R java_version=https://cb-s3-dist.s3.amazonaws.com/java/openjdk/jdk-8-ea-bin-b118-linux-x64-28_nov_2013.tar.gz --type tomcat7 target/*.war
