#!/bin/bash

IMAGE_NAME="snake-webserver"
CONTAINER_NAME="snake-webserver-container"

# Set the temporary folder name
TEMP_FOLDER="tmp"

# Check if the container exists
if docker inspect "$CONTAINER_NAME" &>/dev/null; then
    # Container exists
    echo "Stopping and removing existing container..."
    docker stop "$CONTAINER_NAME" &>/dev/null
    docker rm "$CONTAINER_NAME" &>/dev/null
fi

# Check if the image exists
if docker image inspect "$IMAGE_NAME" &>/dev/null; then
    echo "Deleting existing Docker image: $IMAGE_NAME..."
    docker rmi "$IMAGE_NAME"
fi

# Create temporary folder for the client files (Docker build context issues)
mkdir "$TEMP_FOLDER"

# Copy the ../snake/client directory to the temporary folder
cp -r ../snake/client "$TEMP_FOLDER"

echo "Building Docker image..."
docker build -t "$IMAGE_NAME" .

# Delete the temporary folder
rm -rf "$TEMP_FOLDER"
