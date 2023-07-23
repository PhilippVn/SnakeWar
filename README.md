# SnakeWar
Single and two-player multiplayer snake game featuring an own docker-virtualized webserver and gameserver with private game rooms for you and your friends :) 

# Requirements
- Docker

# Controls:
w,a,s,d for up, left, down and right

# Features
- Snake Game
- Webserver

# To Come:
- Database support

# How to install and run?
- clone the repo
  
## Gameserver
- goto `snake/server`
- build docker image manually or use `build.bat`/`build.sh`
- start the docker container manually or use `start.bat`/`start.sh`
- to stop the game server either kill the container with `docker kill` or press `ctr + c`
- paste the displayed websocket url in `snake/client/game.js` > gameserverIp

## Webserver
- go into `webserver`
- build docker image manually or use `build.bat`/`build.sh`
- run container manually or use `start.bat`/`start.sh`
- copy the displayed Server-Url into your browser and enjoy some SNAKE alone or with friends :)
- to stop the web server either kill the container with `docker kill` or press `ctr + c`

# Change Ports
- for webserver:
    - change the port in `webserver/webserver.py`
    - change the port in `webserver/start.bat`
- for game server:
    - change the port in `snake/server/app/src/SnakeServer.java`
    - change the port in `snake/server/start.bat`
    - update `snake/client/game.js` > gameserverIp

# FAQ
## I get a permission error when trying to execute the shell scripts
The shell scripts are missing the execute flag. Add it with `chmod +x *.sh` in `webserver` and `snake/server`
## I get a gradlew permission error when running the game server container
Same as with the shell scripts: Execute `chmod +x gradlew` in `snake/server`

# Public Gameserver
Public hosted Game instance for single and multiplayer can be found [here](http://212.227.179.145:8000/)
