package server;

import java.util.LinkedList;
import java.util.List;

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
