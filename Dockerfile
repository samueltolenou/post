FROM openjdk
COPY ./target/post-1.jar  app.jar
ENTRYPOINT [ "java","-jar","./app.jar"]