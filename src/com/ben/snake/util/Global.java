package com.ben.snake.util;

import java.io.*;
import java.util.Properties;

public class Global {
    private static Properties properties = new Properties();

    private static String CONFIG_FILE = "snake.properties";

    public static final int CELL_WIDTH;
    public static final int CELL_HEIGHT;
    public static final int WIDTH;
    public static final int HEIGHT;
    public static final int CANVAS_WIDTH;
    public static final int CANVAS_HEIGHT;
    public static final int INIT_LENGTH;
    public static final int SPEED;
    public static final int SPEED_STEP;
    public static final String TITLE_LABEL_TEXT;
    public static final String INFO_LABEL_TEXT;

    private Global() {
    }

    static {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(CONFIG_FILE);
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("没有找到配置文件");
        }finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Integer temp = null;
        WIDTH = (temp = getIntValue("width")) != null && temp <= 80 && temp >= 10 ? temp : 35;
        HEIGHT = (temp = getIntValue("height")) != null && temp <= 60
                && temp >= 10 ? temp : 20;
        INIT_LENGTH = (temp = getIntValue("init_length")) != null && temp > 1
                && temp < WIDTH ? temp : 2;
        SPEED = (temp = getIntValue("speed")) != null && temp >= 10 ? temp
                : 200;
        SPEED_STEP = (temp = getIntValue("speed_step")) != null && temp >= 1 ? temp
                : 25;

        int defaultCellSize = (temp = getIntValue("cell_size")) != null
                && temp > 0 && temp <= 100 ? temp : 20;
        CELL_WIDTH = (temp = getIntValue("cell_width")) != null && temp > 0
                && temp <= 100 ? temp : defaultCellSize;
        CELL_HEIGHT = (temp = getIntValue("cell_height")) != null && temp > 0
                && temp <= 100 ? temp : defaultCellSize;

        CANVAS_WIDTH = WIDTH * CELL_WIDTH;
        CANVAS_HEIGHT = HEIGHT * CELL_HEIGHT;

        String tempStr = null;
        TITLE_LABEL_TEXT = (tempStr = getValue("title")) == null ? "说明："
                : tempStr;
        INFO_LABEL_TEXT = (tempStr = getValue("info")) == null ? "方向键控制方向, 回车键暂停/继续\nPAGE UP, PAGE DOWN 加速或减速\n\n更多请看 www.itcast.cn "
                : tempStr;

    }

    private static Integer getIntValue(String key) {
        if (key == null) {
            throw new RuntimeException("key 不能为空");
        }
        try {
            return new Integer(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String getValue(String key) {
        try {
            String s =  new String(properties.getProperty(key).getBytes("iso8859-1"));
            return s;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
