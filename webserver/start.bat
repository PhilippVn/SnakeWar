@echo off

set IMAGE_NAME=snake-webserver
set CONTAINER_NAME=snake-webserver-container
set PORT=8000

REM Check if the image exists
docker image inspect %IMAGE_NAME% >nul 2>&1
if %errorlevel% neq 0 (
    echo Docker image does not exist. Be sure to build it first!
    exit
)

REM Check if the container already exists
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
REM Run ipconfig and filter the output to extract the IP address
for /f "delims=[] tokens=2" %%a in ('ping -4 -n 1 %ComputerName% ^| findstr [') do set NetworkIP=%%a
echo Server-URL: http://%NetworkIP%:%PORT%

REM Start the container
echo Docker Container is running...
docker run -it --name %CONTAINER_NAME% -p 8000:8000 %IMAGE_NAME%

