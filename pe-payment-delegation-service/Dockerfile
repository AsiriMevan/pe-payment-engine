FROM 172.26.180.20:5000/java-build:LANGUAGE_VERSION as builder
RUN java -version
COPY . /tmp
WORKDIR /tmp
RUN mvn --settings settings.xml clean install -DskipTests ; test $? -eq 0 || exit 1


#FROM dcr.dialog.lk/alpine-jdk-11/alpine-jdk-11:02
FROM 172.26.180.20:5000/java-run:LANGUAGE_VERSION
RUN addgroup -S --gid 2000 appgroup && adduser -S appuser -G appgroup
VOLUME /tmp
USER root
ENV TZ=Asia/Colombo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#ADD target/*.jar app.jar
COPY --from=builder /tmp/target/*.jar app.jar
RUN chown appuser app.jar
USER appuser
ENTRYPOINT [ "java", "-jar", "/app.jar"]
EXPOSE 8080

