FROM java:8
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "app.jar", "-Xms32M -Xmx32M -XX:PermSize=32M -XX:MaxPermSize=64M"]