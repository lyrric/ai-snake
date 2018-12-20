package com.demo.ai.snake.constant;

/**
 * Created on 2018/12/20.
 *
 * @author wangxiaodong
 */
public enum GameStatusEnum {
    /**
     * 游戏状态
     */
    STOP(0,"游戏停止"),
    FIND_PATH(1,"寻路中");
    private int code;
    private String content;

    GameStatusEnum(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }
}
