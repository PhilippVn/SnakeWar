package server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Snake {
    private int length;
    private List<Segment> segments;

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

    public void move(int x, int y, boolean appleEaten) {
        Segment removed = this.segments.remove(0);
        length = this.segments.size();

        Segment newHead = new Segment(x, y, true);
        if (length >= 1)
            this.segments.get(length - 1).setHead(false);
        this.segments.add(newHead);
        this.length = this.segments.size();

        if (appleEaten) {
            // re-add tail if apple is eaten
            removed.setHead(false);
            this.segments.add(0, removed);
            this.length = this.segments.size();
        }
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
        for (Snake.Segment segment : this.segments) {
            if (segment.isHead) {
                continue;
            }
            if (segment.getX() == this.segments.get(this.segments.size() - 1).getX()
                    && segment.getY() == this.segments.get(this.segments.size() - 1).getY()) {
                return true;
            }
        }

        // check if collision with other snake
        for (Snake.Segment segment : other.getSegments()) {
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
}
