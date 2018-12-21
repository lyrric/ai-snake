package com.demo.ai.snake.util;

import com.demo.ai.snake.constant.MapEnum;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.demo.ai.snake.data.GameData.MAP_HEIGHT;
import static com.demo.ai.snake.data.GameData.MAP_WIDTH;

/**
 * Created on 2018/12/21.
 *
 * @author wangxiaodong
 */
public class MapUtil {
    /**
     * 各点到startP的距离
     */
    private int[][] dis;
    /**
     * 地图
     */
    private MapEnum[][] map;
    /**
     * 起点
     */
    private Point startP;
    /**
     * 终点
     */
    private Point endP;
    /**
     * 4个方向移动的向量
     */
    private final int[] dx={1,0,-1,0};
    private final int[] dy={0,1,0,-1};

    public MapUtil(MapEnum[][] map, Point startP, Point endP) {
        this.map = map;
        this.startP = startP;
        this.endP = endP;
        dis = new int[MAP_WIDTH][MAP_HEIGHT];
    }

    /**
     * startP是否可以到达endP
     * @return 是否可以到达
     */
    public boolean isReachable(){
        //是否寻路成功标识
        boolean flag = false;
        Queue<Point> queue = new LinkedBlockingQueue<>();
        //加入起点，开始遍历
        queue.add(startP);
        do{
            //取出头
            Point p = queue.remove();
            if(p.equals(endP)){
                flag = true;
                break;
            }
            //向四个方向探寻
            for(int i=0;i<4;i++){
                Point nextP = new Point(p.x+dx[i],p.y+dy[i]);
                if(nextP.equals(endP)){
                    dis[nextP.x][nextP.y] = dis[p.x][p.y]+1;
                    flag = true;
                    queue = new LinkedBlockingQueue<>();
                    break;
                }
                //下一个坐标是否可以移动
                if((map[nextP.x][nextP.y].equals(MapEnum.EMPTY) || (map[nextP.x][nextP.y].equals(MapEnum.FOOD)))
                        && dis[nextP.x][nextP.y] == 0 && !nextP.equals(startP)){
                    queue.add(nextP);
                    dis[nextP.x][nextP.y] = dis[p.x][p.y]+1;
                }
            }
        }while(queue.size() != 0);
/*        System.out.println("地图到起点距离图");
        for(int i=0; i<MAP_WIDTH; i++){
            for(int j=0; j<MAP_HEIGHT; j++){
                if(i == endP.y && j == endP.x){
                    System.out.print("endP");
                }else if(i == startP.y && j == startP.x){
                    System.out.print("Star");
                }else {
                    System.out.print(String.format("% 4d", dis[j][i]));
                }

            }
            System.out.println();
        }*/
        return flag;
    }
    /**
     * 寻找路径
     * @return
     */
    public Queue<Point> getPath(){
        //查找路径
        List<Point> path = new ArrayList<>(dis[endP.x][endP.y]);
        path.add(endP);
        Point p = endP;

        for(int i=1;i<dis[endP.x][endP.y];i++){
            for(int j=0;j<4;j++){
                if(dis[p.x+dx[j]][p.y+dy[j]] == dis[p.x][p.y]-1){
                    p = new Point( p.x+dx[j], p.y+dy[j]);
                    path.add(p);
                    break;
                }
            }
        }
        Collections.reverse(path);
        return new ConcurrentLinkedQueue<>(path);
    }
}
