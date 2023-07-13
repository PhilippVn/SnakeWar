# Use the official Python base image
FROM ubuntu:latest

# Set the working directory
WORKDIR /app

# Install Java 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk

# Set the JAVA_HOME environment variable
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Set the PATH environment variable to include Java binaries
ENV PATH=${JAVA_HOME}/bin:${PATH}

# Copy all server files into the container workdir
COPY . .

CMD ["./gradlew", "build", "run"]