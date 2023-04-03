# build stage
FROM eclipse-temurin:17 AS builder

COPY src /src

# compile and package the program
RUN javac -d classes src/GuessingGame.java
RUN jar cmvf src/MANIFEST.MF GuessingGame.jar -C classes .


# runtime stage
FROM jlesage/baseimage-gui:debian-11-v4

# install java
ENV DEBIAN_FRONTEND=noninteractive
RUN mkdir -p /usr/share/man/man1 /usr/share/man/man2
RUN apt-get update && apt-get install -y --no-install-recommends openjdk-17-jdk

COPY --from=builder GuessingGame.jar /GuessingGame.jar
COPY startapp.sh /startapp.sh

RUN set-cont-env APP_NAME "Number Guessing Game"
