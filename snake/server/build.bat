@echo off

set IMAGE_NAME=snake-game-server
set CONTAINER_NAME=snake-game-server-container

REM Check if the container exists
docker inspect %CONTAINER_NAME% >nul 2>&1
if %errorlevel% equ 0 (
    REM Container exists
    echo Stopping and removing existing container...
    docker stop %CONTAINER_NAME% >nul
    docker rm %CONTAINER_NAME% >nul
)

REM Check if the image exists
docker image inspect %IMAGE_NAME% >nul 2>&1
if %errorlevel% equ 0 (
    echo Deleting existing Docker image: %IMAGE_NAME%...
    docker rmi %IMAGE_NAME%
)

echo Building Docker image...
docker build -t %IMAGE_NAME% .