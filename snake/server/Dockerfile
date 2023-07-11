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

# Clone the repository and copy the client directory to the container
RUN apt-get install -y git
RUN git clone --single-branch --branch main https://github.com/PhilippVn/SnakeWar.git
RUN cp -R SnakeWar/snake/server/* .
RUN chmod +x ./gradlew
RUN rm -r SnakeWar

CMD ["./gradlew", "build", "run"]