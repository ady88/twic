FROM openjdk:11
COPY . /usr/app/
WORKDIR /usr/app/target
RUN sh -c 'touch twic.jar'
ENTRYPOINT ["java","-jar","twic.jar","--spring.profiles.active=heroku"]