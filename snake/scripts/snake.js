class Snake{
    length;
    direction; // the direction in which the head is looking (e.g. 'north','west,'south','east')
    segments; // array of segments


    constructor(x,y,length,direction){
        this.length = length;
        this.direction = direction;
        this.segments = new Array(new SnakeSegment(x,y,true));
    }

    /**
     * 
     * @param {*} direction 
     * @returns 
     */
    move(direction){
        return direction;
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
}