package com.ben.snake.entities;

import com.ben.snake.util.Global;

import java.awt.*;
import java.util.Random;

public class Food extends Point{

    private Color color = new Color(0xcc0033);

    private Random random = new Random();

    public Food() {
        super();
    }

    public Point getNew() {
        Point p = new Point();
        p.x = random.nextInt(Global.WIDTH);
        p.y = random.nextInt(Global.HEIGHT);
        return p;
    }

    public Food(Point point) {
        super(point);
    }

    public boolean isSnakeEatFood(Snake snake) {
        return this.equals(snake.getHead());
    }

    public void drawMe(Graphics graphics) {
        graphics.setColor(color);

        drawFood(graphics, x * Global.CELL_WIDTH, y * Global.CELL_HEIGHT, Global.CELL_WIDTH, Global.CELL_HEIGHT);
    }

    public void drawFood(Graphics graphics, int x, int y, int width, int height) {
        graphics.draw3DRect(x,y,width,height,true);
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
