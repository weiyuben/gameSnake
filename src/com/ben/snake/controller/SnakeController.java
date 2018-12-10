package com.ben.snake.controller;

import com.ben.snake.entities.Food;
import com.ben.snake.entities.Ground;
import com.ben.snake.entities.Snake;
import com.ben.snake.listener.GameListener;
import com.ben.snake.listener.SnakeListener;
import com.ben.snake.util.Global;
import com.ben.snake.view.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class SnakeController extends KeyAdapter implements SnakeListener {

    private Ground ground;
    private Snake snake;
    private Food food;
    private GamePanel gamePanel;
    private JLabel gameInfoLabel;
    private boolean playing;
    private int map;
    private Set<GameListener> listeners = new HashSet<>();

    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public JLabel getGameInfoLabel() {
        return gameInfoLabel;
    }

    public void setGameInfoLabel(JLabel gameInfoLabel) {
        this.gameInfoLabel = gameInfoLabel;
        this.gameInfoLabel.setSize(Global.WIDTH * Global.CELL_WIDTH, 20);
        this.gameInfoLabel.setFont(new Font("宋体", Font.PLAIN, 12));
        gameInfoLabel.setText(this.getNewInfo());
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }


    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_Y && !playing) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (snake.isPause()) {
                    snake.changePause();
                    for (GameListener l : listeners)
                        l.gameContinue();
                }
                snake.changeDirection(Snake.UP);
                break;
            /* 方向键 下 */
            case KeyEvent.VK_DOWN:
                if (snake.isPause()) {
                    snake.changePause();
                    for (GameListener l : listeners)
                        l.gameContinue();
                }
                snake.changeDirection(Snake.DOWN);
                break;
            /* 方向键 左 */
            case KeyEvent.VK_LEFT:
                if (snake.isPause()) {
                    snake.changePause();
                    for (GameListener l : listeners)
                        l.gameContinue();
                }
                snake.changeDirection(Snake.LEFT);
                break;
            /* 方向键 右 */
            case KeyEvent.VK_RIGHT:
                if (snake.isPause()) {
                    snake.changePause();
                    for (GameListener l : listeners)
                        l.gameContinue();
                }
                snake.changeDirection(Snake.RIGHT);
                break;
            /* 回车或空格 (暂停) */
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                snake.changePause();
                /* === */
                for (GameListener l : listeners)
                    if (snake.isPause())
                        l.gamePause();
                    else
                        l.gameContinue();
                break;
            /* PAGE_UP 加速 */
            case KeyEvent.VK_PAGE_UP:
                snake.speedUp();
                break;
            /* PAGE_DOWN 减速 */
            case KeyEvent.VK_PAGE_DOWN:
                snake.speedDown();
                break;
            /* 字母键 Y (重新开始游戏) */
            case KeyEvent.VK_Y:
                if (!isPlaying())
                    newGame();
                break;
        }

        /* 重新显示 */
        if (gamePanel != null)
            gamePanel.redisplay(ground, snake, food);
        /* 更新提示 */
        if (gameInfoLabel != null)
            gameInfoLabel.setText(getNewInfo());
    }

    public void newGame() {
        if (ground != null) {
            switch (map) {
                case 2:
                    ground.clear();
                    ground.generateRocks2();
                    break;
                default:
                    ground.init();
                    break;
            }
        }
        playing = true;
        snake.reNew();
        for (GameListener l : listeners) {
            l.gameStart();
        }
    }

    private void stopGame() {
        if (playing) {
            playing = false;
            snake.dead();
            for (GameListener l : listeners) {
                l.gameOver();
            }
        }
    }

    @Override
    public void snakeEatFood() {
        System.out.println("🐍吃到食物");
    }

    public void pauseGame() {
        snake.setPause(true);
        for (GameListener l : listeners)
            l.gamePause();
    }

    /**
     * 继续游戏
     */
    public void continueGame() {
        snake.setPause(false);
        for (GameListener l : listeners)
            l.gameContinue();
    }
    public String getNewInfo() {
        if (snake.isLive()) {
            return " ";
        }else
            return new StringBuffer().append("提示：").append("速度").append(snake.getSpeed()).toString()+" 毫秒/格";

    }


    @Override
    public void snakeMove() {
        if (food != null && food.isSnakeEatFood(snake)) {
            snake.eatFood();
            food.setLocation(ground == null ? food.getNew() : ground.getFreePoint());
        } else if (ground != null && ground.isSnakeEatRock(snake)) {
            stopGame();
        }
        if (snake.isEatBody()) {
            stopGame();
        }
        if (gamePanel != null) {
            gamePanel.redisplay(ground, snake, food);
        }
        if (gameInfoLabel != null) {
            gameInfoLabel.setText(getNewInfo());
        }
    }


    public SnakeController(Ground ground, Snake snake, Food food, GamePanel gamePanel) {
        this.snake = snake;
        this.food = food;
        this.gamePanel = gamePanel;
        this.ground = ground;

        if (ground != null && food != null) {
            food.setLocation(ground.getFreePoint());
        }
        this.snake.addSnakeListener(this);
    }

    public SnakeController( Snake snake, Food food, Ground ground,GamePanel gamePanel, JLabel gameInfoLabel) {
        this(ground, snake, food, gamePanel);
        this.setGameInfoLabel(gameInfoLabel);
        if (gameInfoLabel != null) {
            gameInfoLabel.setText(getNewInfo());
        }
    }

    public synchronized void addGameListener(GameListener l) {
        if (l != null)
            this.listeners.add(l);
    }

    /**
     * 移除监听器
     *
     * @param l
     */
    public synchronized void removeGameListener(GameListener l) {
        if (l != null)
            this.listeners.remove(l);
    }

    public boolean isPauseGame() {
        return this.snake.isPause();
    }

}
