# Use the official Python base image
FROM ubuntu:latest

# Set the working directory
WORKDIR /app



# Clone the repository and copy the client directory to the container
RUN apt-get update && apt-get install -y git
RUN apt-get install -y python3
RUN git clone --single-branch --branch main https://github.com/PhilippVn/SnakeWar.git
RUN mkdir serve && cp -r SnakeWar/snake/client serve


# Copy the webserver.py file to the container
RUN mv SnakeWar/webserver/webserver.py .

RUN rm -r SnakeWar

# Start the web server
CMD ["python3", "webserver.py"]