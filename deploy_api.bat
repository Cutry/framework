title bluesky-framework-api
git pull
Call mvn -Dmaven.test.skip=true clean install -pl bluesky-framework-common,bluesky-framework-spring-boot-starter,bluesky-framework-security-spring-boot-starter
Call mvn -Dmaven.test.skip=true clean package -pl bluesky-framework-api
java -jar -Xms500m -Xmx500m bluesky-framework-api/target/bluesky-framework-api-1.0.0-SNAPSHOT.jar --info.domain=192.168.1.30
cmd