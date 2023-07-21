package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public boolean detectCollision(Snake snake) {
        return this.x == snake.getSegments().get(snake.getLength() - 1).getX()
                && this.y == snake.getSegments().get(snake.getLength() - 1).getY();
    }

    public static int[] spawnApple(Snake snake1, Snake snake2) {
        List<int[]> allSpawnLocations = new ArrayList<>();
        List<int[]> possibleSpawnLocations = new ArrayList<>();
        List<Snake.Segment> segments = new ArrayList<>();
        segments.addAll(snake1.getSegments());
        segments.addAll(snake2.getSegments());

        for (int i = 0; i < GameBoard.GAME_HEIGHT / GameBoard.GAME_GRIDSIZE; ++i) {
            for (int j = 0; j < GameBoard.GAME_WIDTH / GameBoard.GAME_GRIDSIZE; ++j) {
                allSpawnLocations.add(new int[] { j * GameBoard.GAME_GRIDSIZE, i * GameBoard.GAME_GRIDSIZE });
            }
        }

        for (int[] e : allSpawnLocations) {
            boolean possible = true;
            for (Snake.Segment segment : segments) {
                if (segment.getX() == e[0] && segment.getY() == e[1]) {
                    possible = false;
                    break;
                }
            }
            if (possible) {
                possibleSpawnLocations.add(e);
            }
        }

        Random random = new Random();
        int r = random.nextInt(possibleSpawnLocations.size());

        return possibleSpawnLocations.get(r);
    }

}
