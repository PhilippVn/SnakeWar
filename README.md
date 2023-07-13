# SnakeWar
Multiplayer snake game

# Requirements
- Docker
- Java 17


# Features
- Snake Game
- Webserver

# To Come:
- multiplayer support
- database

# How to install and run?
- clone the repo
- go into `webserver`
- build docker image manually or use `build.bat`
- run container manually or with `start.bat`
- start the game server by building it with `./gradlew build` and run using `./gradlew run`
- copy the Server-Url into your browser and enjoy some SNAKE :)
- to stop the web server either kill the container with `docker kill` or press `ctr + c`

# Change Ports
- for webserver:
    - change the port in `webserver/webserver.py`
    - change the port in `webserver/start.bat`
- for game server:
    - change the port in `snake/server/app/src/SnakeServer.java`
    - change the port in `snake/server/start.bat`