package com.ben.snake.entities;

import com.ben.snake.listener.SnakeListener;
import com.ben.snake.util.Global;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Snake {
    public static final int UP = 1;
    public static final int DOWN = -1;
    public static final int LEFT = 2;
    public static final int RIGHT = -2;

    private LinkedList<Point> body = new LinkedList<Point>();
    private int oldDirection;
    private int newDirection;
    private Point head;
    private Point tail;
    private int speed;

    private boolean live;
    private boolean pause;

    private Set<SnakeListener> snakeListeners = new HashSet<>();

    public static final Color DEFAULT_HEAD_COLOR = new Color(0xcc0033);
    /* 蛇头的颜色 */
    private Color headColor = DEFAULT_HEAD_COLOR;

    public static final Color DEFAULT_BODY_COLOR = new Color(0xcc0033);
    /* 蛇身体的颜色 */
    private Color bodyColor = DEFAULT_BODY_COLOR;



    public void move() {
        if (oldDirection + newDirection != 0) {
            oldDirection = newDirection;
        }
        tail = (head = takeTail()).getLocation();
        head.setLocation(getHead());
        switch (oldDirection) {
            case UP:
                head.y--;
                if (head.y < 0) {
                    head.y = Global.HEIGHT - 1;
                }
                break;
            case DOWN:
                head.y++;
                if (head.y == Global.HEIGHT) {
                    head.y = 0;
                }
                break;
            case LEFT:
                head.x--;
                /* 到边上了可以从另一边出现 */
                if (head.x < 0)
                    head.x = Global.WIDTH - 1;
                break;
            case RIGHT:
                head.x++;
                /* 到边上了可以从另一边出现 */
                if (head.x == Global.WIDTH)
                    head.x = 0;
                break;
        }
        body.addFirst(head);
    }

    private class SnakeDriver implements Runnable {

        @Override
        public void run() {
            while (live) {
                if (!pause) {
                    move();
                    for (SnakeListener i : snakeListeners) {
                        i.snakeMove();
                    }
                }
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void eatFood() {
        body.addLast(tail.getLocation());
        for (SnakeListener l : snakeListeners) {
            l.snakeEatFood();
        }
    }

    public void changeDirection(int newDirection) {
        this.newDirection = newDirection;
    }
    public Point getHead() {
        return body.getFirst();
    }

    public Point takeTail() {
        return body.removeLast();
    }
    public int getLength() {
        return body.size();
    }
    public void begin() {
        new Thread(new SnakeDriver()).start();
    }

    public void reNew() {
        init();
        begin();
    }

    public void init() {
        body.clear();
        int x = Global.WIDTH/2 - Global.INIT_LENGTH / 2;
        int y = Global.HEIGHT/2;
        for (int i = 0; i < Global.INIT_LENGTH; i++) {
            this.body.addFirst(new Point(x++,y));
        }

        oldDirection = newDirection = RIGHT;
        speed = Global.SPEED;
        live = true;
        pause = false;

    }

    public boolean isEatBody() {
        for (int i = 1; i < body.size(); i++) {
            if (body.getFirst().equals(body.get(i)))
                return true;
        }
        return false;
    }

    public void drawMe(Graphics g) {
        for (Point p : body) {
            g.setColor(bodyColor);
            drawBody(g, p.x * Global.CELL_WIDTH, p.y * Global.CELL_HEIGHT,Global.CELL_WIDTH,Global.CELL_HEIGHT);
        }
        g.setColor(headColor);
        drawHead(g, getHead().x*Global.CELL_WIDTH, getHead().y*Global.CELL_HEIGHT,Global.CELL_WIDTH,Global.CELL_HEIGHT);
    }

    private void drawHead(Graphics g, int x, int y, int width, int height) {
        g.fill3DRect(x,y,width,height,true);
    }

    private void drawBody(Graphics g, int x, int y, int width, int height) {
        g.fill3DRect(x, y, width, height, true);
    }

    public Color getHeadColor() {
        return headColor;
    }

    public void setHeadColor(Color headColor) {
        this.headColor = headColor;
    }

    public Color getBodyColor() {
        return bodyColor;
    }

    public void setBodyColor(Color bodyColor) {
        this.bodyColor = bodyColor;
    }

    public synchronized void addSnakeListener(SnakeListener snakeListener) {
        if (snakeListener == null) return;
        snakeListeners.add(snakeListener);
    }

    public synchronized void removeSnakeListener(SnakeListener snakeListener) {
        if (snakeListener == null) return;
        snakeListeners.remove(snakeListener);
    }

    public void speedUp() {
        if (speed > Global.SPEED_STEP) {
            speed -= Global.SPEED_STEP;
        }
    }
    public void speedDown() {
        speed += Global.SPEED_STEP;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void dead() {
        this.live = false;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void changePause() {
        pause = !pause;
    }
}
