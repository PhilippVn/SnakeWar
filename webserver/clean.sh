#!/bin/bash

IMAGE_NAME="snake-webserver"
CONTAINER_NAME="snake-webserver-container"

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
