window.onload = () => {

    canvas = document.getElementById("canvas");
    ctx = canvas.getContext("2d");

    score = document.getElementById("scorep");

    ctx.lineWidth = 5;
    ctx.strokeStyle = "black";
    ctx.font = 'italic 12pt Calibri';

    setupSinglePlayerGame();

    // add button listeners
    const buttonShowCreateRoom = document.getElementById("button-show-create-room");
    const buttonShowJoinRoom = document.getElementById("button-show-join-room");
    const buttonJoinRoom = document.getElementById("button-join-room");

    buttonShowCreateRoom.addEventListener("click", function () {
        establishGameServerConnection();

        singlePlayer = false;
        document.getElementById("sliders").style.display = "none";
        document.getElementById("createRoomField").style.display = "block";
        document.getElementById("joinRoomField").style.display = "none";
    });

    buttonShowJoinRoom.addEventListener("click", function () {
        singlePlayer = false;
        document.getElementById("sliders").style.display = "none";
        document.getElementById("createRoomField").style.display = "none";
        document.getElementById("joinRoomField").style.display = "block";
        document.getElementById("button-join-room").style.display = "block";
    });

    buttonJoinRoom.addEventListener("click", function () {
        establishRoomConnection();
    });

    // add slider listeners
    const speedslider = document.getElementById("speed");
    speedvalue = document.getElementById("speed-value")

    speedslider.oninput = () => {
        speedvalue.textContent = speedslider.value;
    }
}

function setupSinglePlayerGame() {
    x = canvas.getBoundingClientRect().width / 2 - 25;
    y = canvas.getBoundingClientRect().height / 2 - 25;

    player1Snake = new Snake();
    player1Snake.addSegment(x, y, true);

    appleL = spawnApple(player1Snake.getSegments);
    apple = new Apple(appleL[0], appleL[1]);

    last = performance.now();

    singlePlayer = true;
    window.requestAnimationFrame(gameLoop);
}

function setupMultiPlayerGame() {

}

function establishRoomConnection() {
    if (connectedToRoom)
        return;

    if (!connectedToGameServer) { // TODO check if empty websocket connection is good
        // establish websocket connection to game server but dont do anything
        gameserver = new WebSocket(gameserverIp + '/snake');
    }

    const roomCode = document.getElementById("roomNumberInput").value;
    room = new WebSocket(gameserverIp + "/snake/room/" + roomCode);
    room.onopen = function (event) {
        drawWaitingForPlayers();
        connectedToRoom = true;
        const clientNameMessage = new ClientNameMessage();

        clientNameMessage.messageCode = "client-name"
        clientName = prompt("What is your name?"); // promt user for username
        clientNameMessage.clientName = clientName;
        clientNameMessage.timeStamp = new Date();

        const msg = clientNameMessage.toJson();
        logClientMessage(msg);
        room.send(msg);

        protocolStage = ProtocolStage.SERVER_GAME_START_MESSAGE;
    };

    room.onmessage = function (event) {

        logServerMessage(event.data);


        switch (protocolStage) {
            case ProtocolStage.SERVER_GAME_START_MESSAGE: {
                const msg = ServerGameStartMessage.fromJson(event.data);
                playerNumber = msg.playerNumber;
                protocolStage = ProtocolStage.SERVER_POSITION_UPDATE_MESSAGE;
                break;
            }

            case ProtocolStage.SERVER_POSITION_UPDATE_MESSAGE: {
                // recieve updates
                let msg = ServerPositionUpdateMessage.fromJson(event.data);

                if (msg.messageCode == "game-over") {
                    msg = ServerGameOverMessage.fromJson(event.data);
                    if (msg.winnerExists) {
                        if (msg.winnerName == clientName) {
                            drawWin();
                        } else {
                            drawLoss();
                        }
                    } else {
                        drawTie();
                    }
                    connectedToGameServer = false;
                    connectedToRoom = false;
                    gameserver.close();
                    room.close();
                    return;
                }

                player1Snake = msg.player1Snake;
                player2Snake = msg.player2Snake;
                apple = msg.apple;

                // render changes
                ctx.clearRect(0, 0, canvas.width, canvas.height);
                if (playerNumber == 1) {
                    player1Snake.draw(true);
                    player2Snake.draw(false);
                    score.innerText = `Score: ${player1Snake.getLength}`;
                } else {
                    player1Snake.draw(false);
                    player2Snake.draw(true);
                    score.innerText = `Score: ${player2Snake.getLength}`;
                }

                apple.draw();

                // send new user inputs
                const iMsg = new ClientInputMessage();
                iMsg.messageCode = "client-input";
                iMsg.input = asciiKeyCode;
                iMsg.playerNumber = playerNumber;
                iMsg.timeStamp = new Date();

                const m = iMsg.toJson();
                logClientMessage(m);
                room.send(m);
                keyPressed = false;
                protocolStage = ProtocolStage.SERVER_POSITION_UPDATE_MESSAGE;
                break;
            }

            default: {
                console.error("Unknown protocol stage: " + protocolStage)
            }
        }
    };

    room.onclose = function (event) {
        connectedToRoom = false;
        console.log('WebSocket closed with code:', event.code);
        console.log('Close reason:', event.reason);
        // check for abnormal room close
        if(event.reason == "Room is full"){
            alert("Failed to join room: Room is full");
        }

        if(event.reason == "Invalid Room id"){
            alert("Failed to join room: Invalid Room Id");
        }
    };

    room.onerror = function (event) {
        console.error('WebSocket error:', event);
    };
}

function establishGameServerConnection() {

    if (connectedToGameServer)
        return;

    drawMultiplayerMode();

    gameserver = new WebSocket(gameserverIp + '/snake');

    gameserver.onopen = function (event) {
        console.log('Connected to Game server');
        connectedToGameServer = true;

        // Send a message to the server
        const roomRequestMessage = new ClientRoomRequestMessage();
        roomRequestMessage.messageCode = "room-request"
        roomRequestMessage.timeStamp = new Date();

        const msg = roomRequestMessage.toJson();
        logClientMessage(msg);
        gameserver.send(msg);

    };

    gameserver.onmessage = function (event) {
        // this will be the room id
        logServerMessage(event.data);
        const serverMessage = ServerRoomIdMessage.fromJson(event.data);
        document.getElementById("roomNumber").innerText = "Room Code:" + serverMessage.roomId;
    };

    gameserver.onclose = function (event) {
        connectedToGameServer = false;
        console.log('WebSocket closed with code:', event.code);
        console.log('Close reason:', event.reason);
    };

    gameserver.onerror = function (event) {
        console.error('WebSocket error:', event);
    };
}

function logClientMessage(msg) {
    console.log("Client Message:")
    console.log(msg);
}

function logServerMessage(msg) {
    console.log("Server Message:")
    console.log(msg);
}

function drawMultiplayerMode() {
    ctx.clearRect(0, 0, 1000, 700);
    ctx.fillStyle = "green";
    ctx.font = 'italic 70pt Calibri';
    ctx.fillText("Snake Multiplayer", 0, 100);
}

function drawWaitingForPlayers() {
    drawMultiplayerMode();
    ctx.fillStyle = "grey";
    ctx.font = 'italic 35pt Calibri';
    ctx.fillText("Waiting for players...", 0, 200);
}

function drawWin() {
    ctx.clearRect(0, 0, 1000, 700);
    ctx.fillStyle = "green";
    ctx.font = 'italic 100pt Calibri';
    ctx.fillText("YOU WON!", 200, 300);
    ctx.font = 'italic 50pt Calibri';
    if (playerNumber == 1) {
        ctx.fillText("Final Score: " + player1Snake.getLength, 200, 400);
    } else {
        ctx.fillText("Final Score: " + player2Snake.getLength, 200, 400);
    }

}
function drawLoss() {
    ctx.clearRect(0, 0, 1000, 700);
    ctx.fillStyle = "red";
    ctx.font = 'italic 100pt Calibri';
    ctx.fillText("YOU LOST!", 200, 300);
    ctx.font = 'italic 50pt Calibri';
    if (playerNumber == 1) {
        ctx.fillText("Final Score: " + player1Snake.getLength, 200, 400);
    } else {
        ctx.fillText("Final Score: " + player2Snake.getLength, 200, 400);
    }
}

function drawTie() {
    ctx.fillStyle = "grey";
    ctx.font = 'italic 100pt Calibri';
    ctx.fillText("ITS A TIE!", 200, 300);
    ctx.font = 'italic 50pt Calibri';
    ctx.fillStyle = "black"
    if (playerNumber == 1) {
        ctx.fillText("Final Score: " + player1Snake.getLength, 200, 400);
    } else {
        ctx.fillText("Final Score: " + player2Snake.getLength, 200, 400);
    }
}

// multiplayer
const gameserverIp = "ws://212.227.179.145:51036";
let gameserver; // websocket connection to gameserver
let room; // websocket connection to room
let protocolStage = ProtocolStage.CLIENT_NAME_MESSAGE;
let connectedToGameServer = false;
let connectedToRoom = false;
let playerNumber;
let player2Snake;
let asciiKeyCode;
let clientName;
// single player

let score;

let singlePlayer;

let secondsPassed;
let oldTimeStamp;
let fps;

let stepSize = 25;

let keyPressed; // unused
let key;

let ctx;
let canvas;

let x;
let y;

let last;

let player1Snake;
let apple;

const DEFAULT_GAME_SPEED = 500;
let gameSpeedMultiplier;
let speedvalue;

function gameLoop(timeStamp) {
    secondsPassed = (timeStamp - oldTimeStamp) / 1000;
    oldTimeStamp = timeStamp;
    fps = Math.round(1 / secondsPassed);
    document.getElementById("fps").innerText = "FPS: " + fps;
    document.getElementById("frametime").innerText = "Frametime: " + Math.round((secondsPassed + Number.EPSILON) * 100) / 100;

    gameSpeedMultiplier = speedvalue.textContent;


    if ((timeStamp - last) > (DEFAULT_GAME_SPEED / gameSpeedMultiplier)) {



        // handle user input
        handleUserInput();

        // update


        let appleEaten = apple.detectCollision(x, y);

        let gameOver = player1Snake.move(x, y, appleEaten);

        if (appleEaten) {
            appleL = spawnApple(player1Snake.getSegments);
            apple = new Apple(appleL[0], appleL[1]);
        }






        // paint
        if (!singlePlayer)
            return;

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        //drawGrid(25,25,canvas.getBoundingClientRect().width,canvas.getBoundingClientRect().height);



        player1Snake.draw(true);

        apple.draw();

        if (gameOver) {
            ctx.fillStyle = "red";
            ctx.font = 'italic 125pt Calibri';
            ctx.fillText("GAME OVER", 25, 125);
            return;
        }

        //snake.printSnake();

        score.innerText = `Score: ${player1Snake.getLength}`;

        //ctx.fillStyle = "black";
        //ctx.fillText('X:' + x, 10, 20);
        //ctx.fillText('Y:' + y, 10, 40);

        last = timeStamp;
    }

    window.requestAnimationFrame(gameLoop);
}

function handleUserInput() {
    switch (key) {
        case 'w' || 'W': y -= stepSize; break;
        case 'd' || 'D': x += stepSize; break;
        case 's' || 'S': y += stepSize; break;
        case 'a' || 'A': x -= stepSize; break;
        default: break;//console.error(`Unkown key: ${key}`);
    }

    if (y < 0) {
        y = canvas.getBoundingClientRect().height - 25;
    } else if (y >= canvas.getBoundingClientRect().height) {
        y = 0;
    }

    if (x < 0) {
        x = canvas.getBoundingClientRect().width - 25;
    } else if (x >= canvas.getBoundingClientRect().width) {
        x = 0;
    }
    keyPressed = false;
}

function drawGrid(columnWidth, rowHeight, widthTotal, heightTotal) {
    // first the vertical columns
    ctx.strokeStyle = "red";
    ctx.lineWidth = 2;
    let columns = widthTotal / columnWidth;
    for (i = 0; i <= columns; ++i) {
        ctx.beginPath();
        ctx.moveTo(i * columnWidth, 0);
        ctx.lineTo(i * columnWidth, heightTotal);
        ctx.stroke();
    }

    let rows = heightTotal / rowHeight;

    for (i = 0; i <= rows; ++i) {
        ctx.beginPath();
        ctx.moveTo(0, i * rowHeight);
        ctx.lineTo(widthTotal, i * rowHeight);
        ctx.stroke();
    }
    // second the rows
    ctx.lineWidth = 5;
    ctx.strokeStyle = "black";
}

document.addEventListener('keydown', (event) => {

    const isTextField = event.target.id === "roomNumberInput";

    // If it's the text field, do nothing and return early to ignore the keyboard events
    if (isTextField) {
        return;
    }

    let name = event.key;
    let ascii;

    if (name.length == 1) {
        ascii = name.charCodeAt(0); // get ascii code of key
    }

    if (typeof ascii == "number" && ascii < 128) {
        //console.log(`${name} equals ASCII code ${ascii}`);
        if (name == 'w' || name == 'W' || name == 'd' || name == 'D' || name == 's' || name == 'S' || name == 'a' || name == 'A') {
            keyPressed = true;
            key = name;
            asciiKeyCode = ascii;
        }
    }
    else {
        //console.log( name + " is not in the ASCII character set");
    }

}, false);