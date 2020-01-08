#!/usr/bin/env bash
set +e
git pull
mvn -Dmaven.test.skip=true clean install -pl bluesky-framework-common,bluesky-framework-spring-boot-starter,bluesky-framework-security-spring-boot-starter
mvn -Dmaven.test.skip=true clean package -pl bluesky-framework-api
echo -e "\033[33m \n>>> kill bluesky-framework-api \033[0m"
kill -9 $(lsof -ti tcp:2608)
nohup java -jar -Xms512m -Xmx512m -Duser.timezone=GMT+08 bluesky-framework-api/target/bluesky-framework-api-1.0.0-SNAPSHOT.jar --spring.profiles.active=dev >/dev/null 2>&1 &
echo -e "\033[33m...\033[0m";
echo -e "\033[33m查看日志\033[0m";
sleep 1
tail -f bluesky-framework-api.log