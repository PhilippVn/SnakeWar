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

        // check if collision with itsself
        List<Snake.Segment> extendedSegments = new ArrayList<>();
        extendedSegments.addAll(segments);
        if(removed != null){
            extendedSegments.add(0,removed);
        }
        for (int i = 0; i < extendedSegments.size() - 1; ++i){
            Snake.Segment segment = extendedSegments.get(i);
            if (segment.getX() == this.segments.get(this.segments.size() - 1).getX()
                    && segment.getY() == this.segments.get(this.segments.size() - 1).getY()) {
                return true;
            }
        }

        List<Snake.Segment> extendedOtherSegments = new ArrayList<>();
        extendedOtherSegments.addAll(other.getSegments());
        if(other.getRemoved() != null){
            extendedOtherSegments.add(0,other.getRemoved());
        }
        // check if collision with other snake
        for (int i = 0; i < extendedOtherSegments.size() - 1; ++i){
            Snake.Segment segment = extendedOtherSegments.get(i);
            if (segment.getX() == this.segments.get(this.segments.size() - 1).getX()
                    && segment.getY() == this.segments.get(this.segments.size() - 1).getY()) {
                return true;
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
