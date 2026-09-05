FROM openjdk:21-ea-1-jdk-slim

WORKDIR /app

COPY target/MovimentacaoFinanceira-0.0.1-SNAPSHOT.jar /app/MovimentacaoFinanceira.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/MovimentacaoFinanceira.jar"]