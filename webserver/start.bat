@echo off

set IMAGE_NAME=snake-webserver
set CONTAINER_NAME=snake-webserver-container
set PORT=8000

REM Run ipconfig and filter the output to extract the IP address
for /f "delims=[] tokens=2" %%a in ('ping -4 -n 1 %ComputerName% ^| findstr [') do set NetworkIP=%%a
echo Server-URL: http://%NetworkIP%:%PORT%

REM Start the container
echo Starting Docker container...
docker run -it --name %CONTAINER_NAME% -p 8000:8000 %IMAGE_NAME%