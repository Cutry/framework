chcp 65001
title bluesky-framework-server
git pull
Call mvn -Dmaven.test.skip=true clean install -pl bluesky-framework-common,bluesky-framework-spring-boot-starter
Call mvn -Dmaven.test.skip=true clean package -pl bluesky-framework-core,bluesky-framework-server
java -jar bluesky-framework-server/target/bluesky-framework-server-1.0.0-SNAPSHOT.jar
cmd