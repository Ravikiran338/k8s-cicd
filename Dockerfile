FROM openjdk:8
MAINTAINER Radiant Digital
ADD target/*.jar /msa-user-service.jar
RUN bash -c 'touch /msa-user-service.jar'
CMD ["java","-Dspring.profiles.active=docker","-jar","/msa-user-service.jar"]
EXPOSE 2222
