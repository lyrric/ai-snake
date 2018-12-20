package com.demo.ai.snake.constant;

/**
 * Created on 2018/12/20.
 *
 * @author wangxiaodong
 */
public enum MapEnum {

    /**
     * 墙
     */
    WALL(0),
    /**
     * 空
     */
    EMPTY(1),
    /**
     * 蛇身
     */
    SNAKE_BODY(2),
    /**
     * 蛇头
     */
    SNAKE_HEAD(3),
    /**
     * 十五
     */
    FOOD(4);
    private int code;

    MapEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
