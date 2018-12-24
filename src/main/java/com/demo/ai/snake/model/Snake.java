package com.demo.ai.snake.model;


import java.awt.*;
import java.util.LinkedList;

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
    public Snake() {
        init();
    }

    public void init(){
        body = new LinkedList<>();
        body.addFirst(new Point(3,15));
        body.addFirst(new Point(4,15));
        body.addFirst(new Point(5,15));
        body.addFirst(new Point(6,15));
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

    /**
     * 返回蛇身长度
     * @return
     */
    public int getLength(){
        return body.size();
    }
    public LinkedList<Point> getBody() {
        return body;
    }

}
