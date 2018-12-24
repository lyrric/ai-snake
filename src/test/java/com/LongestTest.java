package com;

import com.demo.ai.snake.constant.MapEnum;
import com.demo.ai.snake.model.Snake;
import com.demo.ai.snake.util.MapUtil;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static com.demo.ai.snake.data.GameData.MAP_HEIGHT;
import static com.demo.ai.snake.data.GameData.MAP_WIDTH;

/**
 * Created on 2018/12/24.
 *
 * @author wangxiaodong
 */
public class LongestTest {
    private MapEnum map[][] = new MapEnum[MAP_WIDTH][MAP_HEIGHT];
    private Point startP = new Point(1,1);
    private Point endP = new Point(8,8);
    /**
     * 4个方向移动的向量
     */
    private static final int[] dx={1,0,-1,0};
    private static final int[] dy={0,1,0,-1};
    private void  LongestTest111() {
        for(int i = 0;i<MAP_WIDTH;i++){
            for(int j = 0;j<MAP_HEIGHT;j++){
                if(i==0 || j == 0|| i==MAP_WIDTH-1 || j == MAP_HEIGHT-1){
                    map[i][j] = MapEnum.WALL;
                }else{
                    map[i][j] = MapEnum.EMPTY;
                }

            }

        }
    }
    public void ITest(){
        LongestTest111();
        MapUtil mapUtil = new MapUtil(map, startP, endP);
        mapUtil.isReachable();
        LinkedList<Point> path = (LinkedList<Point>)mapUtil.getShortestPath();
        path.add(0,startP);
        for(Point p:path){
            map[p.x][p.y] = MapEnum.SNAKE_BODY;
        }
        for(int i=0;i<path.size()-1;){
            Point p1 = path.get(i);
            Point p2 = path.get(i+1);
            int j;
            for(j=0;j<4;j++){
                if(map[p1.x+dx[j]][p1.y+dy[j]] == MapEnum.EMPTY && map[p2.x+dx[j]][p2.y+dy[j]] == MapEnum.EMPTY){
                    break;
                }
            }
            if(j < 4){
                Point newP1 = new Point(p1.x+dx[j],p1.y+dy[j]);
                Point newP2 = new Point(p2.x+dx[j],p2.y+dy[j]);
                path.add(i+1, newP1);
                path.add(i+2, newP2);
                map[newP1.x][newP1.y] = MapEnum.SNAKE_BODY;
                map[newP2.x][newP2.y] = MapEnum.SNAKE_BODY;
            }else{
                i++;
            }
        }
        System.out.println("地图");
        for(int i=0; i<MAP_WIDTH; i++){
            for(int j=0; j<MAP_HEIGHT; j++){
                if(map[j][i] == MapEnum.WALL){
                    System.out.print("wall ");
                }else if(map[j][i] == MapEnum.SNAKE_BODY){
                    System.out.print("body ");
                }else {
                    System.out.print("---- ");
                }

            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        new LongestTest().ITest();
    }
}
