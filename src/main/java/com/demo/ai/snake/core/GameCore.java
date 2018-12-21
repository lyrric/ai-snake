package com.demo.ai.snake.core;

import com.demo.ai.snake.constant.GameStatusEnum;
import com.demo.ai.snake.constant.MapEnum;
import com.demo.ai.snake.data.GameData;
import com.demo.ai.snake.model.Snake;
import com.demo.ai.snake.ui.GamePanel;
import com.demo.ai.snake.util.MapUtil;
import com.demo.ai.snake.util.SimulateGame;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
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
            //指令集，路径
            Queue<Point> path = new ConcurrentLinkedQueue<>();
            gameData.setGameStatus(GameStatusEnum.FIND_PATH);
            //先生成食物
            gameData.randomFood();
            while (true) {
                if (path.size() == 0) {
                    //路走完了，重新找去食物的路
                    System.out.println("路走完了，重新寻路中...");
                    MapUtil mapUtil = new MapUtil(gameData.getMapCopy(), snake.getHead(), gameData.getFoodPoint());
                    if (mapUtil.isReachable()) {
                        //有路去食物，需要判断吃了食物后，能不能找到尾巴
                        path = mapUtil.getPath();
                        //模拟游戏，能找到蛇尾则放行去吃蛇尾
                        if(!new SimulateGame(snake, path).isSafe()){
                            //吃了食物之后找不到蛇尾，则先向尾巴方向前进一步
                            System.out.println("吃了食物之后找不到蛇尾，则先向尾巴方向前进一步");
                            mapUtil = new MapUtil(gameData.getMapCopy(), snake.getHead(), snake.getTail());
                            mapUtil.isReachable();
                            Queue<Point> tempPath = mapUtil.getPath();
                            path = new ConcurrentLinkedQueue<>();
                            path.add(tempPath.remove());
                        }
                    } else {
                        //找不到去食物的路
                        System.out.println("寻路失败，找不到去食物的路");
                        //尝试跟着尾巴走,走一步就重新找一下去食物的路
                        mapUtil = new MapUtil(gameData.getMapCopy(), snake.getHead(), snake.getTail());
                        if (mapUtil.isReachable()) {
                            //有路去尾巴，则向尾巴方向前进，前进一步计算重新计算
                            Queue<Point> tempPath = mapUtil.getPath();
                            path = new ConcurrentLinkedQueue<>();
                            path.add(tempPath.remove());
                        } else {
                            //找不到去尾巴的路，结束游戏
                            System.out.println("找不到去尾巴的路，结束游戏");
                            gameData.setGameStatus(GameStatusEnum.STOP);
                            return;
                        }
                    }
                }
                Point newHead = path.remove();
                switch (gameData.getPoint(newHead.x, newHead.y)){
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
                snake.addFirst(newHead);
                gameData.update();
                gamePanel.repaint();
                try {
                    Thread.sleep(1000 - gameData.getSpeed() * 100 - 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
