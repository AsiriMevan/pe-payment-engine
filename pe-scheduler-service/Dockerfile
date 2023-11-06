FROM 172.26.180.20:5000/java-build:LANGUAGE_VERSION as builder
#FROM dte-admin.dialog.lk/registry/java-build:LANGUAGE_VERSION as builder
#FROM dcr.dialog.lk/maven/3-adoptopenjdk-11:01 as builder
WORKDIR /application
COPY . /application
RUN mvn --settings settings.xml clean install -DskipTests ; test $? -eq 0 || exit 1
#RUN mvn dependency:go-offline
#RUN mvn package -B -DskipTests
ARG PROJECT_KEY
ARG PROJECT_NAME
ARG RELEASE_TYPE
#RUN mvn verify sonar:sonar -Dsonar.projectKey=$PROJECT_KEY-$RELEASE_TYPE -Dsonar.projectName=$PROJECT_NAME-$RELEASE_TYPE -Dsonar.exclusions="src/test/**,src/main/java/lk/dialog/pe/scheduler/soap/custom/**" -Dmaven.test.skip=true
ARG JAR_FILE=/application/target/*.jar
 
FROM 172.26.180.20:5000/java-run:LANGUAGE_VERSION
#FROM openjdk:8-jdk-alpine
#FROM dcr.dialog.lk/alpine-jdk-11/alpine-jdk-11:04
RUN addgroup -S --gid 2000 appgroup && adduser -S appuser -G appgroup
WORKDIR application
COPY --from=builder application/target/*.jar ./app.jar
USER appuser
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Colombo", "-Djava.security.auth.login.config=/kafka-res/jaas.conf", "-Djava.security.krb5.conf=/kafka-res/krb5.conf", "app.jar"]
EXPOSE 8080

