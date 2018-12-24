package com.demo.ai.snake.core;

import com.demo.ai.snake.constant.GameStatusEnum;
import com.demo.ai.snake.data.GameData;
import com.demo.ai.snake.model.Snake;
import com.demo.ai.snake.ui.GamePanel;
import com.demo.ai.snake.util.MapUtil;
import com.demo.ai.snake.util.SimulateGame;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
    public GameCore(GameData gameData, GamePanel gamePanel) {
        this.gameData = gameData;
        this.gamePanel = gamePanel;
        snake = gameData.getSnake();
    }

    /**
     * 返回游戏是否运行成功
     * @return
     */
    public boolean startGame(){
        if(gameData.getGameStatus().equals(GameStatusEnum.STOP)){
            gameThread = new GameThread();
            gameData.init();
            gameThread.start();
            return true;
        }
        return false;
    }


    private class GameThread extends Thread {

        @Override
        public void run() {
            //下一个路径
            //是否在追蛇尾
            boolean flag = false;
            Queue<Point> path = new LinkedBlockingQueue<>();
            gameData.setGameStatus(GameStatusEnum.FIND_PATH);
            //先生成食物
            gameData.randomFood();
            gameData.update();
            gamePanel.repaint();
            while (true) {
                if(path.size() == 0){
                    //找去食物的路
                    MapUtil mapUtil = new MapUtil(gameData.getMapCopy(), snake.getHead(), gameData.getFoodPoint());
                    //有路去食物，并且吃了食物之后能找到蛇尾
                    List<Point> tempPath;
                    if (mapUtil.isReachable() && (tempPath = new SimulateGame(snake, mapUtil).getSafePath()) != null) {
                        //放行
                        path = new LinkedBlockingQueue<>(tempPath);

                        //每次计算最新路径
                        //path.add(mapUtil.getShortestPath().remove(0));
                    } else {
                        //找不到去食物的路，或者吃了食物之后找不到蛇尾
                        //尝试跟着尾巴走,走一步就重新找一下去食物的路
                        mapUtil = new MapUtil(gameData.getMapCopy(), snake.getHead(), snake.getTail());
                        if (mapUtil.isReachable()) {
                            //有路去尾巴，则向尾巴方向前进，前进一步计算重新计算
                            System.out.println("找不到去食物的路，跟随尾巴前进");
                            tempPath = mapUtil.getShortestPath();
                            Point nextP = tempPath.get(0);
                            //需要判断下，头和尾相邻的情况，直接前行会撞上
                            if(snake.getTail().equals(nextP)){
                                //如果头尾相邻，则随机走一步空的位置
                                System.out.println("如果头尾相邻，则随机走一步空的位置");
                                nextP = MapUtil.getAdjacentEmptyPoint(snake.getHead(), gameData.getMapCopy());
                                if(nextP==null){
                                    //死定了
                                    System.out.println("死定了");
                                    gameData.setGameStatus(GameStatusEnum.STOP);
                                    return;
                                }
                                path.add(nextP);
                            }else{
                                path = new LinkedBlockingQueue<>(tempPath);
                            }
                        } else {
                            //找不到去尾巴的路，结束游戏（不会发生）
                            System.out.println("找不到去尾巴的路，结束游戏");
                            gameData.setGameStatus(GameStatusEnum.STOP);
                            return;
                        }
                    }
                }

                Point nextP = path.remove();
                switch (gameData.getPoint(nextP.x, nextP.y)){
                    case WALL:
                        //撞墙了，游戏结束
                        System.out.println("撞墙了，游戏结束");
                        gameData.setGameStatus(GameStatusEnum.STOP);
                        return;
                    case SNAKE_BODY:
                        //撞到自己了，游戏结束
                        System.out.println("撞到自己了，游戏结束");
                        gameData.setGameStatus(GameStatusEnum.STOP);
                        return;
                    case FOOD:
                        //撞到食物了，游戏结...
                        // 加分,重新生成食物
                        gameData.scoreInc();
                        gameData.randomFood();
                        break;
                     default:
                         snake.removeTail();
                }
                //添头去尾，实现移动
                snake.addFirst(nextP);
                gameData.update();
                gamePanel.repaint();
                try {
                    Thread.sleep(1000 - gameData.getSpeed() * 100 + 50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
