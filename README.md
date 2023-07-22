# SnakeWar
Single and two-player multiplayer snake game featuring an own docker-virtualized webserver and gameserver with private game rooms for you and your friends :) 

# Requirements
- Docker

# Features
- Snake Game
- Webserver

# To Come:
- Database support
- Public hosted web- and gameserver

# How to install and run?
- clone the repo
## Gameserver
- goto `snake/server`
- build docker image manually or use `build.bat`
- start the docker container manually or use `start.bat`
- to stop the game server either kill the container with `docker kill` or press `ctr + c`
- paste the displayed websocket url in `snake/client/game.js` > gameserverIp

## Webserver
- go into `webserver`
- build docker image manually or use `build.bat`
- run container manually or use `start.bat`
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
