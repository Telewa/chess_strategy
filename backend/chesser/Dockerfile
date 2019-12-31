# base image
FROM openjdk:11-jdk

# Dockerfile config
LABEL Name="chesser-backend"
LABEL Version="1.0.0"
LABEL Maintainer="Emmanuel Telewa <emmanuelt2009@gmail.com>"

# add the code
WORKDIR /app
COPY . .

# expose default port for Spring
EXPOSE 8080

# trigger the download of the binaries before copying and building the application
RUN ./gradlew --version

# run the spring boot app
CMD ./gradlew bootRun