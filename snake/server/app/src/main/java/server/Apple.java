package server;

public class Apple {
    private int x;
    private int y;

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

    public Apple() {
    }

    public Apple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] spawnApple(Snake snake1, Snake snake2) {
        return new int[] { 1, 2 };
    }

}
