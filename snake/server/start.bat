@echo off
setlocal enabledelayedexpansion

set IMAGE_NAME=snake-game-server
set CONTAINER_NAME=snake-game-server-container
set PORT=51036

REM Check if the image exists
docker image inspect %IMAGE_NAME% >nul 2>&1
if %errorlevel% neq 0 (
    echo Docker image does not exist. Be sure to build it first!
    exit
)

REM Check if the container already exists TODO: make it restart container instead of deleting it
docker inspect %CONTAINER_NAME% >nul 2>&1
if %errorlevel% equ 0 (
    REM Container exists
    docker inspect --format="{{.State.Status}}" %CONTAINER_NAME% | findstr /C:"exited" >nul
    if %errorlevel% equ 0 (
        REM Container has exited, so stop and remove it
        echo Stopping and removing existing container...
        docker stop %CONTAINER_NAME% >nul
        docker rm %CONTAINER_NAME% >nul
    )else (
        REM Container is running, do nothing
        echo Container %CONTAINER_NAME% is already running.
        exit /b
    )
)


REM Get the default gateway IP address
for /f "tokens=3" %%a in ('route print ^| findstr "\<0.0.0.0\>"') do set "DefaultGateway=%%a"

REM Get the IP address of the interface associated with the default gateway
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr "!DefaultGateway!"') do set "NetworkIP=%%a"

REM Remove leading and trailing spaces from the IP address
set "NetworkIP=%NetworkIP: =%"
set "NetworkIP=%NetworkIP::=%"

echo Server-URL: ws://%NetworkIP%:%PORT%

REM Start the container
echo Docker Container is running...
docker run -it --name %CONTAINER_NAME% -p %PORT%:%PORT% %IMAGE_NAME%

