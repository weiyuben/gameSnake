package com.ben.snake.view;

import com.ben.snake.entities.Food;
import com.ben.snake.entities.Ground;
import com.ben.snake.entities.Snake;
import com.ben.snake.util.Global;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class GamePanel extends JPanel {
    private Image gImage;
    private Graphics gg;

    public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0xcfcfcf);
    private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;

    public GamePanel() {
        this.setSize(Global.WIDTH * Global.CELL_WIDTH, Global.HEIGHT * Global.CELL_HEIGHT);
        this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        this.setFocusable(true);
    }


    public synchronized void redisplay(Ground ground, Snake snake, Food food) {
        if (gg == null) {
            gImage = createImage(getSize().width, getSize().height);
            if (gImage != null)
                gg = gImage.getGraphics();
        }
        if (gg != null) {
            gg.setColor(backgroundColor);
            gg.fillRect(0,0, Global.WIDTH * Global.CELL_WIDTH, Global.HEIGHT
                    * Global.CELL_HEIGHT);
            if (ground != null)
                ground.drawMe(gg);
            snake.drawMe(gg);
            if (food != null)
                food.drawMe(gg);
            this.paint(this.getGraphics());
        }

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(gImage, 0, 0, this);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
