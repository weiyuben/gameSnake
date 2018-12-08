package com.ben.snake.entities;

import com.ben.snake.listener.SnakeLister;
import com.ben.snake.util.Global;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Snake {
    private static final int UP = 1;
    private static final int DOWN = -1;
    private static final int LEFT = 2;
    private static final int RIGHT = -2;

    private LinkedList<Point> body = new LinkedList<Point>();
    private int oldDirection;
    private int newDirection;
    private Point head;
    private Point tail;
    private int speed;

    private boolean live;
    private boolean pause;

    private Set<SnakeLister> snakeListers = new HashSet<>();

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
                    for (SnakeLister i : snakeListers) {
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
    public Food getHead() {
        return null;
    }

    public Point takeTail() {
        return body.removeLast();
    }
}
