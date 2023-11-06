FROM dte-admin.dialog.lk/registry/java-build:LANGUAGE_VERSION as builder
WORKDIR /application
COPY . /application
#RUN mvn dependency:go-offline
#RUN mvn package -B -DskipTests
RUN mvn --settings settings.xml clean install -DskipTests ; test $? -eq 0 || exit 1
ARG PROJECT_KEY
ARG PROJECT_NAME
ARG RELEASE_TYPE
#RUN mvn verify sonar:sonar -Dsonar.projectKey=$PROJECT_KEY-$RELEASE_TYPE -Dsonar.projectName=$PROJECT_NAME-$RELEASE_TYPE -Dsonar.exclusions=src/test/** -Dmaven.test.skip=true
ARG JAR_FILE=/application/target/*.jar
 
FROM dte-admin.dialog.lk/registry/java-run:LANGUAGE_VERSION
RUN addgroup -S --gid 2000 appgroup && adduser -S appuser -G appgroup
WORKDIR application
COPY --from=builder application/target/*.jar ./app.jar

# Create the necessary directories for log files
RUN mkdir -p /logs/pe-other-payment-service/archived

# Set the ownership of the directories to the non-root user
RUN chown -R appuser:appgroup /logs/pe-other-payment-service

USER appuser
ENTRYPOINT ["java", "-Duser.timezone=Asia/Colombo","-jar","app.jar"]
EXPOSE 8080
