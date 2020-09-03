FROM openjdk:14-jdk-slim AS build

ARG SBT_VERSION=1.3.13

RUN apt -y update && apt install -y curl
RUN curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
    dpkg -i sbt-$SBT_VERSION.deb && \
    rm sbt-$SBT_VERSION.deb
RUN apt update && \
    apt install sbt && \
    sbt sbtVersion
COPY . /build-app
WORKDIR /build-app
RUN SBT_OPTS="-Xms512M -Xmx1024M -Xss2M -XX:MaxMetaspaceSize=1024M" sbt assembly

# -------------------------------------------------------------------------------------

FROM openjdk:14-jdk-slim
COPY --from=build /build-app/target/scala-2.13/scala-learn-assembly-0.1.jar ./app.jar
CMD java -jar app.jar