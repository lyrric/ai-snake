package com.demo.ai.snake.util;

import com.demo.ai.snake.constant.MapEnum;
import com.demo.ai.snake.model.Snake;
import com.sun.corba.se.impl.orbutil.ObjectStreamClassUtil_1_3;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.demo.ai.snake.constant.MapEnum.*;
import static com.demo.ai.snake.data.GameData.MAP_HEIGHT;
import static com.demo.ai.snake.data.GameData.MAP_WIDTH;

/**
 * Created on 2018/12/21.
 * 模拟游戏,判断蛇吃掉食物后是否还能找到蛇尾
 * @author wangxiaodong
 */
public class SimulateGame {
    /**
     * 蛇
     */
    private Snake snake;
    /**
     * 去食物的路径
     */
    private ArrayList<Point> path;
    /**
     * 空地图
     */
    private MapEnum[][] map;

    public SimulateGame(Snake snake, Queue<Point> path) {
        this.snake = snake;
        this.path = new ArrayList<>(path.size());
        this.path.addAll(path);
        Collections.reverse(this.path);
        //初始化地图
        map = new MapEnum[MAP_WIDTH][MAP_HEIGHT];
        for(int i=0;i<MAP_WIDTH;i++){
            for(int j=0;j<MAP_HEIGHT;j++){
                if(i==0 || i==MAP_WIDTH-1 || j==0 || j== MAP_HEIGHT -1){
                    map[i][j] = WALL;
                }else{
                    map[i][j] = EMPTY;
                }
            }
        }
    }
    public boolean isSafe(){
        int length = snake.getLength();
        //吃掉食物后的蛇身
        LinkedList<Point> newSnakeBody = new LinkedList<>();
        //蛇沿着path吃掉食物，如果路径比蛇身短，那么新的蛇身会包含所有的path,并且身体有一部分的位置是以前蛇身所在位置
        for(int i=0;i<length+1 && path.size() != 0;i++){
            newSnakeBody.addLast(path.remove(0));
        }
        //如果路径比蛇身短,加上原来蛇身的相应部分
        List<Point> body = snake.getBody();
        for(int i=0;newSnakeBody.size() < length+1;i++){
            newSnakeBody.addLast(body.get(i));
        }
        //将蛇身映射在地图上
        newSnakeBody.forEach(point -> map[point.x][point.y]=SNAKE_BODY);
        //判断蛇头能否找到蛇尾
        //如果蛇头和蛇尾相邻，则判断为不能找到蛇尾
        if(MapUtil.isAdjacent(newSnakeBody.getFirst(),newSnakeBody.getLast())){
            return false;
        }
        MapUtil mapUtil = new MapUtil(map, newSnakeBody.getFirst(), newSnakeBody.getLast());
        return mapUtil.isReachable();
        /*
        System.out.println("地图");
        for(int i=0; i<MAP_WIDTH; i++){
            for(int j=0; j<MAP_HEIGHT; j++){
                if(map[j][i].equals(SNAKE_BODY)){
                    System.out.print("body");
                }else if(map[j][i].equals(FOOD)){
                    System.out.print("food");
                }else if(map[j][i].equals(WALL)){
                    System.out.print("wall");
                }else{
                    System.out.print("empt");
                }

            }
            System.out.println();
        }*/
    }
}
