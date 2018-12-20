package com.demo.ai.snake.constant;

/**
 * Created on 2018/12/20.
 * 蛇方向枚举类
 * @author wangxiaodong
 */
public enum SnakeDirectionEnum {
    /**
     * 上
     */
    UP(1),
    /**
     * 下
     */
    DOWN(2),
    /**
     * 左
     */
    LEFT(3),
    /**
     * 右
     */
    RIGHT(4);

    private int code;

    SnakeDirectionEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
