package com.demo.ai.snake.model;


import com.demo.ai.snake.constant.SnakeDirectionEnum;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2018/12/18.
 *
 * @author wangxiaodong
 */

public class Snake {
    /**
     * 蛇身数据
     */
    private LinkedList<Point> body;
    /**
     * 蛇方向
     */
    private SnakeDirectionEnum direction;
    public Snake() {
        init();
    }

    public void init(){
        direction = SnakeDirectionEnum.RIGHT;
        body = new LinkedList<>();
        body.addFirst(new Point(3,17));
        body.addFirst(new Point(4,17));
        body.addFirst(new Point(5,17));
        body.addFirst(new Point(6,17));
    }

    /**
     * 移除尾巴
     */
    public void removeTail(){
        body.removeLast();
    }

    /**
     * 从头加入元素
     * @param head
     */
    public void addFirst(Point head){
        body.addFirst(head);
    }

    /**
     * 获得头
     * @return
     */
    public Point getHead(){
        return body.getFirst();
    }
    /**
     * 获得尾巴
     * @return
     */
    public Point getTail(){
        return body.getLast();
    }
    public SnakeDirectionEnum getDirection() {
        return direction;
    }

    public void setDirection(SnakeDirectionEnum direction) {
        this.direction = direction;
    }

    public LinkedList<Point> getBody() {
        return body;
    }

}
