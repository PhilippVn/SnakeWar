@echo off

set IMAGE_NAME=snake-webserver

REM Check if the image exists
docker image inspect %IMAGE_NAME% >nul 2>&1
if %errorlevel% neq 0 (
    echo Building Docker image...
    docker build -t %IMAGE_NAME% .
) else (
    echo Docker image already exists. Be sure to delete the image: %IMAGE_NAME%!
    exit
)