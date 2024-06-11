FROM openjdk:11
EXPOSE 6088
ADD target/product-service.jar product-service.jar
ENTRYPOINT ["java","-jar","/product-service.jar"]