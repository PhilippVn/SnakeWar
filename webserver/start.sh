#!/bin/bash

IMAGE_NAME="snake-webserver"
CONTAINER_NAME="snake-webserver-container"
PORT=8000

# Check if the image exists
if ! docker image inspect "$IMAGE_NAME" &>/dev/null; then
    echo "Docker image does not exist. Be sure to build it first!"
    exit 1
fi

# Check if the container already exists
if docker inspect "$CONTAINER_NAME" &>/dev/null; then
    # Container exists
    if docker inspect --format='{{.State.Status}}' "$CONTAINER_NAME" | grep -q 'exited'; then
        # Container has exited, so stop and remove it
        echo "Stopping and removing existing container..."
        docker stop "$CONTAINER_NAME" &>/dev/null
        docker rm "$CONTAINER_NAME" &>/dev/null
    else
        # Container is running, do nothing
        echo "Container $CONTAINER_NAME is already running."
        exit 0
    fi
fi

# Run ifconfig and filter the output to extract the IP address
NetworkIP=$(ip route get 1 | awk '{print $NF; exit}')
echo "Server-URL: http://$NetworkIP:$PORT/"

# Start the container
echo "Docker Container is running..."
docker run -it --name "$CONTAINER_NAME" -p "$PORT":"$PORT" "$IMAGE_NAME"