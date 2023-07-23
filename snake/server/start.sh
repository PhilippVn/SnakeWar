#!/bin/bash

IMAGE_NAME="snake-game-server"
CONTAINER_NAME="snake-game-server-container"
PORT=51036

# Check if the image exists
if ! docker image inspect "$IMAGE_NAME" &>/dev/null; then
    echo "Docker image does not exist. Be sure to build it first!"
    exit 1
fi

# Check if the container already exists
if docker inspect "$CONTAINER_NAME" &>/dev/null; then
    # Container exists
    docker inspect --format='{{.State.Status}}' "$CONTAINER_NAME" | grep -q 'exited'
    if [ $? -eq 0 ]; then
        # Container has exited, so stop and remove it
        echo "Stopping and removing existing container..."
        docker stop "$CONTAINER_NAME" &>/dev/null
        docker rm "$CONTAINER_NAME" &>/dev/null
    else
        # Container is running, restart it
        echo "Container $CONTAINER_NAME is already running. Restarting..."
        docker restart "$CONTAINER_NAME" &>/dev/null
        exit 0
    fi
fi

# Run ifconfig and filter the output to extract the IP address
DefaultGateway=$(ip route | awk '/default/ {print $3}')
NetworkIP=$(ifconfig | grep -B1 "$DefaultGateway" | awk '/inet / {print $2}' | cut -d ':' -f2)
echo "Server-URL: ws://$NetworkIP:$PORT"

# Start the container
echo "Docker Container is running..."
docker run -it --name "$CONTAINER_NAME" -p "$PORT":"$PORT" "$IMAGE_NAME"