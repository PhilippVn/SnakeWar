# Use the official Python base image
FROM ubuntu:latest

# Set the working directory
WORKDIR /app

# Create the destination directory
RUN mkdir serve

# Copy Webserver files from temp folder (build script) into container workdir/serve
COPY tmp/. ./serve/
COPY webserver.py .

RUN apt-get update && apt-get install -y python3

# Start the web server
CMD ["python3", "webserver.py"]