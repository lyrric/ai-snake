package com.demo.ai.snake.core;

import com.demo.ai.snake.constant.GameStatusEnum;
import com.demo.ai.snake.constant.MapEnum;
import com.demo.ai.snake.data.GameData;
import com.demo.ai.snake.model.Snake;
import com.demo.ai.snake.ui.GamePanel;

import java.awt.*;

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
            gameData.setGameStatus(GameStatusEnum.FIND_PATH);
            //先生成食物
            gameData.randomFood();
            while(true){
                Point oldHead = snake.getHead();
                Point tail = snake.getTail();
                Point newHead = new Point(oldHead.x+1, oldHead.y);
                if(gameData.getPoint(newHead.x, newHead.y).equals(MapEnum.WALL)){
                    //撞墙了，游戏结束
                    gameData.setGameStatus(GameStatusEnum.STOP);
                    return ;
                }else if(gameData.getPoint(newHead.x, newHead.y).equals(MapEnum.SNAKE_BODY) && !tail.equals(newHead)){
                    //撞到自己了，游戏结束
                    gameData.setGameStatus(GameStatusEnum.STOP);
                    return ;
                }
                snake.addFirst(newHead);
                snake.removeTail();
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

}
