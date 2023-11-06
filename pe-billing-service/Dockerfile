FROM dte-admin.dialog.lk/registry/java-build:LANGUAGE_VERSION as builder
WORKDIR /application
COPY . /application
RUN mvn --settings settings.xml clean install -DskipTests ; test $? -eq 0 || exit 1

FROM dte-admin.dialog.lk/registry/java-run:LANGUAGE_VERSION
RUN addgroup -S --gid 2000 appgroup && adduser -S appuser -G appgroup
VOLUME /tmp
USER root
ENV TZ=Asia/Colombo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY --from=builder application/target/*.jar app.jar
RUN chown appuser app.jar
USER appuser
ENTRYPOINT [ "java", "-jar", "/app.jar"]
EXPOSE 8080
