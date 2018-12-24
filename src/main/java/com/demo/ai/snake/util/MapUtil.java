package com.demo.ai.snake.util;

import com.demo.ai.snake.constant.MapEnum;
import com.demo.ai.snake.model.Snake;

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
    private static final int[] dx={1,0,-1,0};
    private static final int[] dy={0,1,0,-1};

    public MapUtil(MapEnum[][] map, Point startP, Point endP) {
        this.map = map;
        this.startP = startP;
        this.endP = endP;
        dis = new int[map.length][map[0].length];
    }

    /**
     * startP是否可以到达endP
     * @return 是否可以到达
     */
    public boolean isReachable(){
        //是否寻路成功标识
        Queue<Point> queue = new LinkedBlockingQueue<>();
        //加入起点，开始遍历
        queue.add(startP);
        do{
            //取出头
            Point p = queue.remove();
            //向四个方向探寻
            for(int i=0;i<4;i++){
                Point nextP = new Point(p.x+dx[i],p.y+dy[i]);
                if(nextP.equals(endP)){
                    dis[nextP.x][nextP.y] = dis[p.x][p.y]+1;
                    return true;
                }
                //下一个坐标是否可以移动
                if((map[nextP.x][nextP.y].equals(MapEnum.EMPTY) || (map[nextP.x][nextP.y].equals(MapEnum.FOOD)))
                        && dis[nextP.x][nextP.y] == 0 && !nextP.equals(startP)){
                    queue.add(nextP);
                    dis[nextP.x][nextP.y] = dis[p.x][p.y]+1;
                }
            }
        }while(queue.size() != 0);
        return false;
    }
    /**
     * 寻找路径
     * @return
     */
    public List<Point> getShortestPath(){
        return getShortestPath(0);
    }
    /**
     * 寻找路径
     * @param direction 优先方向
     * @return
     */
    public List<Point> getShortestPath(int direction){
        //查找路径
        List<Point> path = new LinkedList<>();
        path.add(endP);
        Point p = endP;
        for(int i=1;i<dis[endP.x][endP.y];i++){
            int temp = 1;
            for(int j=direction;temp<5;j++,temp++){
                if(j == 4){j=0;}
                if(dis[p.x+dx[j]][p.y+dy[j]] == dis[p.x][p.y]-1){
                    p = new Point( p.x+dx[j], p.y+dy[j]);
                    path.add(p);
                    break;
                }
            }
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * 获取最长路径，近似算法
     * @return
     */
    public List<Point> getLongestPath(){
        List<Point> path = getShortestPath();
        //加入头
        path.add(0,startP);
        for(Point p:path){
            map[p.x][p.y] = MapEnum.SNAKE_BODY;
        }
        for(int i=0;i<path.size()-1;){
            Point p1 = path.get(i);
            Point p2 = path.get(i+1);
            int j;
            //往四个方向扩展边
            for(j=0;j<4;j++){
                if(map[p1.x+dx[j]][p1.y+dy[j]] == MapEnum.EMPTY
                        && map[p2.x+dx[j]][p2.y+dy[j]] == MapEnum.EMPTY ){
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
        //去掉头
        path.remove(0);
        return path;
    }
    /**
     * 判断两个点是否相邻
     * @param p1
     * @param p2
     * @return
     */
    public static boolean isAdjacent(Point p1, Point p2){
        for(int i=0;i<4;i++){
            if(new Point(p1.x+dx[i], p1.y+dy[i]).equals(p2)){
                return true;
            }
        }
        return false;
    }

    /**
     * 获得地图上位于坐标点周围空的一点
     * @param p
     * @param map
     * @return
     */
    public static Point getAdjacentEmptyPoint(Point p, MapEnum[][] map){
        for(int i=0;i<4;i++){
            if(map[p.x+dx[i]][p.y+dy[i]].equals(MapEnum.EMPTY)){
                return new Point(p.x+dx[i],p.y+dy[i]);
            }
        }
        return null;
    }
}
