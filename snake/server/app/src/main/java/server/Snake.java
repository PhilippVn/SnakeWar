package server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Snake {
    private int length;
    private List<Segment> segments;
    private Snake.Segment removed;

    public Snake(int x, int y) {
        this.length = 1;
        this.segments = new LinkedList<>();
        this.segments.add(new Segment(x, y, true));
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public void move(int x, int y) {
        // if the snake didnt move do nothing
        if(this.segments.get(this.segments.size()-1).getX() == x && this.segments.get(this.segments.size()-1).getY() == y){
            return;
        }
        this.removed = this.segments.remove(0);
        length = this.segments.size();

        Segment newHead = new Segment(x, y, true);
        if (length >= 1)
            this.segments.get(length - 1).setHead(false);
        this.segments.add(newHead);
        this.length = this.segments.size();
    }

    // re-add removed tail if apple is eaten
    public void addRemoved(){
        this.removed.setHead(false);
        this.segments.add(0,  this.removed);
        this.length = this.segments.size();
    }

    /**
     * returns coordinates of the first snake head
     * 
     * @param other other snake or null
     * @return
     * @see server.Apple.spawnApple
     */
    public static int[] spawnSnake(Snake other) {
        List<int[]> allSpawnLocations = new ArrayList<>();
        List<int[]> possibleSpawnLocations = new ArrayList<>();

        for (int i = 0; i < GameBoard.GAME_HEIGHT / GameBoard.GAME_GRIDSIZE; ++i) {
            for (int j = 0; j < GameBoard.GAME_WIDTH / GameBoard.GAME_GRIDSIZE; ++j) {
                allSpawnLocations.add(new int[] { j * GameBoard.GAME_GRIDSIZE, i * GameBoard.GAME_GRIDSIZE });
            }
        }

        if (other == null) {
            possibleSpawnLocations = allSpawnLocations;
        } else {
            Snake.Segment otherHead = other.getSegments().get(other.getSegments().size() - 1);
            for (int[] arr : allSpawnLocations) {
                if (arr[0] != otherHead.getX() && arr[1] != otherHead.getY()) {
                    possibleSpawnLocations.add(arr);
                }
            }
        }

        // choose random Spawn Location
        Random random = new Random();
        int r = random.nextInt(possibleSpawnLocations.size());

        return possibleSpawnLocations.get(r);
    }
    

    /**
     * checks if the head of this sneak collided with itsself or the other snake
     * 
     * @param other
     * @return true if collision
     */
    public boolean detectCollision(Snake other) {

        // problem: segments going through each other without colliding
        // happens if snake of length two goes though itsself or when two snakes of length 1 who are direct neighbours go against each other
        // => check removed segment

        // check if collision with itsself
        Snake.Segment myHead = this.segments.get(this.segments.size()-1);
        if(this.segments.size() == 2){
            if(removed != null){
                if((myHead.getX() == removed.getX()) && (myHead.getY() == removed.getY())){
                    return true;
                }
            }
        }else{
            // check if head collides with any other segment
            for(int i = 0; i < this.segments.size() - 1;++i){
                Snake.Segment segment = this.segments.get(i);
                if(segment.isHead()){
                    continue;
                }
                if((myHead.getX() == segment.getX()) && (myHead.getY() == segment.getY())){
                    return true;
                }
            }
        }

        Snake.Segment otherHead = other.getSegments().get(other.getSegments().size() -1);
        // check if collision with other snake
        if((this.segments.size() == 1) && (other.getSegments().size() == 1) && (this.removed != null) && (other.getRemoved() != null)){
            // they went through each other when both heads ended up on the removed segment of the other snake
            // if one of them is null one didnt move -> couldnt have gone through each other -> no edge case
                if((otherHead.getX() == this.removed.getX()) && (otherHead.getY() == this.removed.getY()) 
                && (myHead.getX() == other.getRemoved().getX()) && (myHead.getY() == other.getRemoved().getY())){
                    return true;
                }
        }else{
            // check if head collides with any segment from the other snake
            for(int i = 0; i < other.getSegments().size(); ++i){
                Snake.Segment segment = other.getSegments().get(i);
                if(segment.isHead()){
                    continue;
                }
                if((myHead.getX() == segment.getX()) && (myHead.getY() == segment.getY())){
                    return true;
                }
            }
        }
        return false;
    }

    public class Segment {
        private int x;
        private int y;
        private boolean isHead;

        public Segment(int x, int y, boolean isHead) {
            this.x = x;
            this.y = y;
            this.isHead = isHead;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public boolean isHead() {
            return isHead;
        }

        public void setHead(boolean isHead) {
            this.isHead = isHead;
        }

    }

    public Snake.Segment getRemoved() {
        return removed;
    }
}
