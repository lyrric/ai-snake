package com.demo.ai.snake.data;

import com.demo.ai.snake.constant.GameStatusEnum;
import com.demo.ai.snake.constant.MapEnum;
import com.demo.ai.snake.model.Snake;

import java.awt.*;
import java.util.Random;

import static com.demo.ai.snake.constant.MapEnum.*;

/**
 * Created on 2018/12/18.
 * 数据
 * @author wangxiaodong
 */
public class GameData {
    /**
     * 地图宽
     */
    public static final int MAP_WIDTH = 19;
    /**
     * 地图高
     */
    public static final int MAP_HEIGHT = 19;
    /**
     * 地图
     */
    private MapEnum[][] map = new MapEnum[MAP_WIDTH][MAP_HEIGHT];
    /**
     * 游戏分数
     */
    private int score;
    /**
     * 游戏速度
     * 1->10
     */
    private int speed;
    /**
     * 张益达
     */
    private Snake snake;
    /**
     * 游戏状态
     */
    private GameStatusEnum gameStatus;
    private Point foodPoint;

    public GameData() {
        snake = new Snake();
        init();
    }

    /**
     * 初始化地图
     */
    public void init(){
        score = 0;
        speed = 10;
        snake.init();
        gameStatus = GameStatusEnum.STOP;
        for(int i=0;i<MAP_WIDTH;i++){
            for(int j=0;j<MAP_HEIGHT;j++){
                if(i==0 || i==MAP_WIDTH-1 || j==0 || j== MAP_HEIGHT -1){
                    map[i][j] = WALL;
                }else{
                    map[i][j] = EMPTY;
                }
            }
        }
        addSnakeToMap();

    }

    /**
     * 获得某个点的数据
     * @param x
     * @param y
     * @return
     */
    public MapEnum getPoint(int x, int y){
        return map[x][y];
    }
    /**
     * 将蛇头，蛇身映射在地图上
     */
    private void addSnakeToMap(){
        if(snake != null){
            snake.getBody().forEach(point -> map[point.x][point.y]=SNAKE_BODY);
            map[snake.getHead().x][snake.getHead().y]=SNAKE_HEAD;
        }
    }
    /**
     * 将食物，蛇身映射在地图上
     */
    private void addFoodToMap(){
        if(foodPoint != null){
            map[foodPoint.x][foodPoint.y] = FOOD;
        }
    }
    /**
     * 获得游戏分数
     * @return
     */
    public int getScore(){
        return score;
    }

    /**
     * score++
     */
    public void  scoreInc() {
        score++;
    }



    public GameStatusEnum getGameStatus() {
        return gameStatus;
    }

    public int getSpeed() {
        return speed;
    }

    /**
     * 生成食物坐标点
     */
    public void randomFood(){
        foodPoint = null;
        //生成随机数在1-18，排除了围墙
        int x = (int)(1+Math.random()*(MAP_WIDTH-1));
        int y = (int)(1+Math.random()*(MAP_HEIGHT-1));
        //从随机的位置向后开始查找空位，确定食物位置
        //第一排（横坐标为x）排为特殊情况，只查找y之后的位置
        int i, j;
        for(j = x;j<MAP_HEIGHT;j++){
            if(map[j][y].equals(MapEnum.EMPTY)){
                foodPoint = new Point(j,y);
                return;
            }
        }
        //如果第x排没有找到，则从下一排开始找，如果是最后一排，需要从第二排开始找
        for(i = ((y+1)>=MAP_WIDTH)?1:y+1;i<MAP_WIDTH;i++){
            for(j=1;j<MAP_WIDTH;j++){
                if(map[i][j].equals(MapEnum.EMPTY)){
                    foodPoint = new Point(i,j);
                    return ;
                }
            }
           if(i >= MAP_WIDTH-1){
                i = 1;
           }
        }
    }
    /**
     * 更新蛇身和食物
     */
    public void update(){
        for(int i=0;i<MAP_WIDTH;i++){
            for(int j=0;j<MAP_HEIGHT;j++){
                if(i==0 || i==MAP_WIDTH-1 || j==0 || j== MAP_HEIGHT -1){
                    map[i][j] = WALL;
                }else{
                    map[i][j] = EMPTY;
                }
            }
        }
        addFoodToMap();
        addSnakeToMap();
    }

    /**
     * 获得地图的拷贝
     *
     * @return
     */
    public MapEnum[][] getMapCopy() {
        MapEnum[][] mapCopy = new MapEnum[MAP_WIDTH][MAP_HEIGHT];
        for(int i=0;i<map.length;i++){
            System.arraycopy(map[i], 0, mapCopy[i], 0, map[i].length);
        }
        return mapCopy;
    }
    public Point getFoodPoint() {
        return foodPoint;
    }

    public void setGameStatus(GameStatusEnum gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Snake getSnake() {
        return snake;
    }


}
