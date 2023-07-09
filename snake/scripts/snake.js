class Snake{
    length;
    segments; // array of segments


    constructor(x,y){
        this.length = 1;
        this.segments = new Array(new SnakeSegment(x,y,true));
    }

    detectCollision(x,y){
        if(this.segments.length == 1){
            return false;
        }
        for(i = 0; i < this.segments.length - 1;++i){
            if(this.segments[i].getX == x && this.segments[i].getY == y){
                return true;
            }
        }
        return false;
    }


    move(x, y, appleEaten) {

        let collision = this.detectCollision(x,y);

        const removed = this.segments.shift();
        this.length = this.segments.length;

        const newHead = new SnakeSegment(x,y,true);
        if(this.length >= 1)
            this.segments[this.length-1].setIsHead = false;
        this.segments.push(newHead);
        this.length = this.segments.length;

        if(appleEaten){
            removed.setIsHead = false;
            this.segments.unshift(removed);
            this.length = this.segments.length;
        }

        return collision;
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
        ctx.fillRect(this.x,this.y,20,20); // 24 so there is a little gap :)
    }
}