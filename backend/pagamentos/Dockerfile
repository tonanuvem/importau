# Dockerfile para o microsserviço de Pagamentos
FROM eclipse-temurin:17-jdk-alpine

# Metadados
LABEL maintainer="Equipe IMPORTAÚ <dev@importau.com>"
LABEL description="Microsserviço de Pagamentos - IMPORTAÚ"
LABEL version="1.0.0"

# Instala Maven
RUN apk add --no-cache maven

# Diretório de trabalho
WORKDIR /app

# Copia arquivos de configuração do Maven
COPY pom.xml .

# Baixa dependências (cache layer)
RUN mvn dependency:go-offline -B

# Copia código fonte
COPY src ./src

# Compila a aplicação
RUN mvn clean package -DskipTests

# Expõe a porta da aplicação
EXPOSE 8083

# Configurações de JVM para container
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC"

# Comando para executar a aplicação
CMD ["java", "-jar", "target/pagamentos-service-1.0.0.jar"]
