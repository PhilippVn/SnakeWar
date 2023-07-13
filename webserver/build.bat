@echo off

set IMAGE_NAME=snake-webserver
set CONTAINER_NAME=snake-webserver-container

REM Set the temporary folder name
SET TEMP_FOLDER=tmp


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

REM Create temporary folder for the client files (Docker buildcontext issues)
mkdir %TEMP_FOLDER%

REM Copy the ../snake/client directory to the temporary folder
xcopy /E /I /Y ..\snake\client %TEMP_FOLDER%

echo Building Docker image...
docker build -t %IMAGE_NAME% .

REM Delete the temporary folder
RD /S /Q %TEMP_FOLDER%