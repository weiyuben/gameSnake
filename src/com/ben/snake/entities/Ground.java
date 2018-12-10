package com.ben.snake.entities;

import com.ben.snake.util.Global;
import sun.org.mozilla.javascript.internal.GeneratedClassLoader;

import java.awt.*;
import java.util.Random;

public class Ground extends Point {
    private boolean rocks[][] = new boolean[Global.WIDTH][Global.HEIGHT];
    private Point freePoint = new Point();
    public static final Color DEFAULT_ROCK_COLOR = new Color(0x666666);
    /* 石头的颜色 */
    private Color rockColor = DEFAULT_ROCK_COLOR;

    public static final Color DEFAULT_GRIDDING_COLOR = Color.LIGHT_GRAY;

    private Color griddingColor = DEFAULT_GRIDDING_COLOR;
    private Random random = new Random();
    private boolean drawGridding = false;

    public Ground() {
        init();
    }

    public void init() {
        clear();
        generateRocks();
    }

    public void clear() {
        for (int i = 0; i < Global.WIDTH; i++) {
            for (int j = 0; j < Global.HEIGHT; j++) {
                rocks[i][j] = false;
            }
        }
    }
    public void generateRocks() {
        for (int x = 0; x < Global.WIDTH; x++)
            rocks[x][0] = rocks[x][Global.HEIGHT - 1] = true;
        for (int y = 0; y < Global.HEIGHT; y++)
            rocks[0][y] = rocks[Global.WIDTH - 1][y] = true;
    }

    public void generateRocks2() {

        for (int y = 0; y < 6; y++) {
            rocks[0][y] = true;
            rocks[Global.WIDTH - 1][y] = true;
            rocks[0][Global.HEIGHT - 1 - y] = true;
            rocks[Global.WIDTH - 1][Global.HEIGHT - 1 - y] = true;
        }
        for (int y = 6; y < Global.HEIGHT - 6; y++) {
            rocks[6][y] = true;
            rocks[Global.WIDTH - 7][y] = true;
        }
    }

    public void addRock(int x, int y) {
        rocks[x][y] = true;
    }

    public boolean isSnakeEatRock(Snake snake) {
        return rocks[snake.getHead().x][snake.getHead().y];
    }

    public Point getFreePoint() {
        do {
            freePoint.x = random.nextInt(Global.WIDTH);
            freePoint.y = random.nextInt(Global.HEIGHT);
        } while (rocks[freePoint.x][freePoint.y]);
        return freePoint;
    }

    public Color getRockColor() {
        return rockColor;
    }

    public void setRockColor(Color rockColor) {
        this.rockColor = rockColor;
    }


    public void drawMe(Graphics g) {
        for (int x = 0; x < Global.WIDTH; x++) {
            for (int y = 0; y < Global.HEIGHT; y++) {
                if (rocks[x][y]) {
                    drawRock(g, x * Global.CELL_WIDTH, y * Global.CELL_HEIGHT, Global.CELL_WIDTH, Global.CELL_HEIGHT);
                } else if (drawGridding) {
                    drawGridding(g,x * Global.CELL_WIDTH, y * Global.CELL_HEIGHT, Global.CELL_WIDTH, Global.CELL_HEIGHT);
                }
            }
        }
    }

    private void drawRock(Graphics g,int x,int y,int width, int height) {
        g.fill3DRect(x,y,width,height,true);
    }

    private void drawGridding(Graphics g, int x, int y, int width, int height) {
        g.drawRect(x,y,width,height);
    }
    public Color getGriddingColor() {
        return griddingColor;
    }

    public void setGriddingColor(Color griddingColor) {
        this.griddingColor = griddingColor;
    }

    public boolean isDrawGridding() {
        return drawGridding;
    }

    public void setDrawGridding(boolean drawGridding) {
        this.drawGridding = drawGridding;
    }
}

