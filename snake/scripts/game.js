window.onload  = () => {

    canvas = document.getElementById("canvas");
    ctx = canvas.getContext("2d");

    score = document.getElementById("scorep");
    ctx.lineWidth = 5;
    ctx.strokeStyle = "black";
    ctx.font = 'italic 12pt Calibri';

    x = canvas.getBoundingClientRect().width/2 - 25;
    y = canvas.getBoundingClientRect().height/2 - 25;

    snake = new Snake(x,y);

    appleL = spawnApple(snake.getSegments);
    apple = new Apple(appleL[0],appleL[1]);

    last = performance.now();

    window.requestAnimationFrame(gameLoop);
}

let score;

let secondsPassed;
let oldTimeStamp;
let fps;

let stepSize = 25;

let keyPressed;
let key;

let ctx;
let canvas;

let x;
let y;

let last;

let snake;
let apple;

const DEFAULT_GAME_SPEED = 500;
let gameSpeedMultiplier = 2.5;

function gameLoop(timeStamp){
    secondsPassed = (timeStamp - oldTimeStamp) / 1000;
    oldTimeStamp = timeStamp;
    fps = Math.round(1 / secondsPassed);
    document.getElementById("fps").innerText = "FPS: " + fps;
    document.getElementById("frametime").innerText = "Frametime: " + Math.round((secondsPassed + Number.EPSILON) * 100) / 100;


    if((timeStamp - last) > (DEFAULT_GAME_SPEED/gameSpeedMultiplier)){



        // handle user input
        handleUserInput();

        // update
        
        let appleEaten = apple.detectCollision(x,y);

        if(appleEaten){
            appleL = spawnApple(snake.getSegments);
            apple = new Apple(appleL[0],appleL[1]);
        }

        let gameOver = snake.detectCollision();
        snake.move(x,y,appleEaten);

        if(gameOver){
            ctx.strokeStyle = "red";
            ctx.font = 'italic 125pt Calibri';
            ctx.fillText("GAME OVER",25,125);
            return;
        }

        // paint

        ctx.clearRect(0,0,canvas.width,canvas.height);
        //drawGrid(25,25,canvas.getBoundingClientRect().width,canvas.getBoundingClientRect().height);

        snake.draw();

        apple.draw();

        //snake.printSnake();

        score.innerText = `Score: ${snake.getLength}`;
        
        ctx.fillStyle = "black";
        ctx.fillText('X:' + x,10,20);
        ctx.fillText('Y:' + y,10,40);
        ctx.fillStyle = "green";
        
        last = timeStamp;
    }

    
    window.requestAnimationFrame(gameLoop);
}

function handleUserInput(){
    switch(key){
        case 'w' || 'W' || "ArrowUp": y -= stepSize;break;
        case 'd' || 'D' || "ArrowRight": x += stepSize;break;
        case 's' || 'S' || "ArrowDown": y += stepSize;break;
        case 'a' || 'A' || "ArrowLeft": x -= stepSize;break;
        default: break;//console.error(`Unkown key: ${key}`);
    }

    if(y < 0) {
        y = canvas.getBoundingClientRect().height - 25;
    }else if(y >= canvas.getBoundingClientRect().height){
        y = 0;
    }

    if(x < 0){
        x = canvas.getBoundingClientRect().width - 25;
    }else if(x >= canvas.getBoundingClientRect().width){
        x = 0;
    }
    keyPressed = false;
}

function drawGrid(columnWidth,rowHeight,widthTotal,heightTotal){
    // first the vertical columns
    ctx.strokeStyle = "red";
    ctx.lineWidth = 2;
    let columns = widthTotal / columnWidth;
    for(i = 0; i <= columns;++i){
        ctx.beginPath();
        ctx.moveTo(i*columnWidth,0);
        ctx.lineTo(i*columnWidth,heightTotal);
        ctx.stroke();
    }

    let rows = heightTotal / rowHeight;

    for(i = 0; i <= rows;++i){
        ctx.beginPath();
        ctx.moveTo(0,i*rowHeight);
        ctx.lineTo(widthTotal,i*rowHeight);
        ctx.stroke();
    }
    // second the rows
    ctx.lineWidth = 5;
    ctx.strokeStyle = "black";
}

document.addEventListener('keydown', (event) => {
    var name = event.key;
    var code = event.code;
    // Alert the key name and key code on keydown+
    console.log(name)
    if(name == 'w' || name == 'W' || 'ArrowUp' || name == 'd' || name == 'D' || 'ArrowRight' || name == 's' || name == 'S' || 'ArrowDown' || name == 'a' || name == 'A' || 'ArrowLeft'){
        if(!keyPressed){
            keyPressed = true;
            key = name;
        }
    }

}, false);