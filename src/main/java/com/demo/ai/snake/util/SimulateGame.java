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
     * map
     */
    private MapUtil mapUtil;
    /**
     * 空地图
     */
    private MapEnum[][] map;

    public SimulateGame(Snake snake, MapUtil mapUtil) {
        this.snake = snake;
        this.mapUtil = mapUtil;
        //初始化地图
        initMap();
    }
    private void initMap(){
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
    /**
     * 获取安全路径，返回null表示无安全路径
     * @return
     */
    public List<Point> getSafePath(){
        for(int k=0;k<4;k++){
            //初始化地图
            if(k == 1){
                System.out.println("11");
            }
            initMap();
            int length = snake.getLength();
            //吃掉食物后的蛇身
            LinkedList<Point> newSnakeBody = new LinkedList<>();
            //蛇沿着path吃掉食物，如果路径比蛇身短，那么新的蛇身会包含所有的path,并且身体有一部分的位置是以前蛇身所在位置
            List<Point> path = mapUtil.getShortestPath(k);
            for(int i=0;i<length+1 && i < path.size();i++){
                newSnakeBody.addFirst(path.get(i));
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
            if(!MapUtil.isAdjacent(newSnakeBody.getFirst(),newSnakeBody.getLast())){
                MapUtil mapUtil = new MapUtil(map, newSnakeBody.getFirst(), newSnakeBody.getLast());
               if(mapUtil.isReachable()){
                   return path;
               }
            }

        }
        return null;
    }
}
