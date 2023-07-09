# Use the official Python base image
FROM ubuntu:latest

# Set the working directory
WORKDIR /app



# Clone the repository and copy the client directory to the container
RUN apt-get update && apt-get install -y git
RUN apt-get install -y python3
RUN git clone https://github.com/PhilippVn/SnakeWar.git
RUN mkdir serve && mv SnakeWar/snake/client serve
RUN rm -r SnakeWar


# Copy the webserver.py file to the container
COPY webserver.py /app/

# Start the web server
CMD ["python3", "webserver.py"]