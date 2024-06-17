FROM bellsoft/liberica-openjdk-alpine:17 as builder

RUN apk update && apk add --no-cache git

WORKDIR /home/

ARG REPO_URL=https://github.com/hbam1/dizzycode.git
RUN git clone ${REPO_URL} app

WORKDIR /home/app/

RUN ./gradlew clean build

FROM bellsoft/liberica-openjdk-alpine:17

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]