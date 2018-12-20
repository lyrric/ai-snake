package com.demo.ai.snake.core;

import com.demo.ai.snake.constant.GameStatusEnum;
import com.demo.ai.snake.constant.MapEnum;
import com.demo.ai.snake.data.GameData;
import com.demo.ai.snake.model.Snake;
import com.demo.ai.snake.ui.GamePanel;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.demo.ai.snake.data.GameData.MAP_HEIGHT;
import static com.demo.ai.snake.data.GameData.MAP_WIDTH;

/**
 * Created on 2018/12/20.
 * 游戏核心
 * @author wangxiaodong
 */
public class GameCore {
    /**
     * 游戏数据
     */
    private final GameData gameData;
    /**
     * 游戏面板，用于控制刷新
     */
    private final GamePanel gamePanel;
    /**
     * 蛇数据
     */
    private final Snake snake;
    /**
     * 游戏线程，控制游戏运行
     */
    private GameThread gameThread;
    /**
     * 4个方向移动的向量
     */
    private final int[] dx={1,0,-1,0};
    private final int[] dy={0,1,0,-1};

    public GameCore(GameData gameData, GamePanel gamePanel) {
        this.gameData = gameData;
        this.gamePanel = gamePanel;
        snake = gameData.getSnake();
        gameThread = new GameThread();
    }

    /**
     * 返回游戏是否运行成功
     * @return
     */
    public boolean startGame(){
        if(gameData.getGameStatus().equals(GameStatusEnum.STOP)){
            gameData.init();
            gameThread.start();
            return true;
        }
        return false;
    }


    private class GameThread extends Thread{

        @Override
        public void run() {
            //指令集，路径
            Queue<Point> path = new LinkedBlockingQueue<>();
            gameData.setGameStatus(GameStatusEnum.FIND_PATH);
            //先生成食物
            gameData.randomFood();
            while(true){
                if(path.size() == 0){
                    //路走完了，重新找路
                    path = findPath(gameData.getFoodPoint());
                    System.out.println("路走完了，重新寻路中...");
                    //情况1，找不到去食物的路
                    if(path == null){
                        System.out.println("寻路失败，找不到去食物的路");
                        //尝试跟着尾巴走,走一步就重新找一下去食物的路
                        Queue<Point> tempPath = findPath(snake.getTail());
                        //情况2，找不到尾巴
                        if(tempPath == null){
                            System.out.println("寻路失败，找不到去尾巴的路");
                            gameData.setGameStatus(GameStatusEnum.STOP);
                            return ;
                        }
                        path = new LinkedBlockingQueue<>();
                        path.add(tempPath.remove());
                    }
                }
                Point tail = snake.getTail();
                Point newHead = path.remove();
                if(gameData.getPoint(newHead.x, newHead.y).equals(MapEnum.WALL)){
                    //撞墙了，游戏结束
                    System.out.println("撞墙了，游戏结束");
                    gameData.setGameStatus(GameStatusEnum.STOP);
                    return ;
                }else if(gameData.getPoint(newHead.x, newHead.y).equals(MapEnum.SNAKE_BODY) && !tail.equals(newHead)){
                    //撞到自己了，游戏结束
                    System.out.println("撞到自己了，游戏结束");
                    gameData.setGameStatus(GameStatusEnum.STOP);
                    return ;
                }else if(newHead.equals(gameData.getFoodPoint())){
                    //加分,重新生成食物
                    gameData.scoreInc();
                    gameData.randomFood();
                }else{
                    snake.removeTail();
                }
                //添头去尾，实现移动
                snake.addFirst(newHead);
                gameData.update();
                gamePanel.repaint();
                try {
                    Thread.sleep(1000-gameData.getSpeed()*100-100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 寻找路径
     * @return
     */
    private Queue<Point> findPath(Point endP){
       //地图上各点到蛇首的距离
        int[][] dis = new int[MAP_WIDTH][MAP_HEIGHT];
        for(int i= 0;i<MAP_WIDTH;i++){
            for(int j=0;j<MAP_HEIGHT;j++){
                dis[i][j]=0;
            }
        }
        //是否寻路成功标识
        boolean flag = false;
        Point startP = snake.getHead();

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
                //下一个坐标是否可以移动
                if((gameData.getPoint(nextP.x, nextP.y).equals(MapEnum.EMPTY) || gameData.getPoint(nextP.x, nextP.y).equals(MapEnum.FOOD)) && dis[nextP.x][nextP.y] == 0 && !nextP.equals(startP)){
                    queue.add(nextP);
                    dis[nextP.x][nextP.y] = dis[p.x][p.y]+1;
                }
            }

        }while(queue.size() != 0);
        if(!flag){
            return null;
        }
/*        System.out.println("    地图到起点距离图");
        for(int i=0; i<MAP_WIDTH; i++){
            for(int j=0; j<MAP_HEIGHT; j++){
                if(i == endP.y && j == endP.x){
                    System.out.print("****");
                }else{
                    System.out.print(String.format("% 4d", dis[j][i]));
                }

            }
            System.out.println();
        }*/
        //查找路径
        LinkedList<Point> path = new LinkedList<>();
        path.addFirst(endP);
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
        return path;
    }

}
