class Snake{
    length;
    segments; // array of segments


    constructor(x,y){
        this.length = 1;
        this.segments = new Array(new SnakeSegment(x,y,true));
    }

    detectCollision(){
        if(this.segments.length == 1){
            return false;
        }
        let head = this.segments[this.segments.length - 1];
        for(i = 0; i < this.segments.length - 1;++i){
            if(this.segments[i].getX == head.getX && this.segments[i].getY == head.getY){
                return true;
            }
        }
        return false;
    }


    move(x,y,appleEaten){
        if(this.segments.length < 1){
            alert(`Invalid Snake Size!`);
        }

        if(x == this.segments[0].getX && y == this.segments[0].getY){
            return;
        }

        if(this.segments.length == 1){
            if(!appleEaten){
                this.segments[0].setX = x;
                this.segments[0].setY = y;
            }else{
                this.segments[this.segments.length - 1].setIsHead =false;
                this.length = this.segments.push(new SnakeSegment(x,y,true));
            }
        }else{
            if(!appleEaten){
                this.segments.shift();
            }
            this.segments[this.segments.length - 1].setIsHead =false;
            this.length = this.segments.push(new SnakeSegment(x,y,true));
        }
    }

    get getLength(){
        return this.length;
    }

    get getSegments(){
        return this.segments;
    }

    draw(){
        for(i = 0; i < this.segments.length; ++i){
            this.segments[i].draw();
        }
    }

    printSnake() {
        console.log("-----------------SNAKE length: " + this.segments.length + "-----------------------")
        console.log(this.segments.toString());
        for(i = 0; i < this.segments.length;i++){
            if(this.segments[i].isHead){
                console.log(`HEAD: X: ${this.segments[i].getX} Y:${this.segments[i].getY}`);
            }else{
                console.log(`TAIL: X: ${this.segments[i].getX} Y:${this.segments[i].getY}`);
            }
        }
    }
}

class SnakeSegment{
    x;
    y;
    isHead;

    constructor(x,y,isHead){
        this.x = x;
        this.y = y;
        this.isHead = isHead;
    }

    get getX(){
        return this.x;
    }

    get getY(){
        return this.y;
    }

    set setX(newX){
        this.x = newX;
    }

    set setY(newY){
        this.y = newY;
    }

    get getIsHead(){
        return this.isHead;
    }

    set setIsHead(isHead){
        this.isHead = isHead;
    }

    draw(){
        if(this.isHead){
            ctx.fillStyle = "green";
        }else{
            ctx.fillStyle = "greenyellow"
        }
        ctx.fillRect(this.x,this.y,25,25);
    }
}