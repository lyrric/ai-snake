package com.demo.ai.snake.ui;

import com.demo.ai.snake.constant.MapEnum;
import com.demo.ai.snake.core.GameCore;
import com.demo.ai.snake.data.GameData;

import javax.swing.*;
import java.awt.*;

import static com.demo.ai.snake.constant.MapEnum.*;
import static com.demo.ai.snake.data.GameData.*;

/**
 * Created on 2018/12/20.
 *
 * @author wangxiaodong
 */
public class GamePanel extends JPanel {
    private JButton startBtn;
    private JButton speedUpBtn;
    private JButton speedDownBtn;
    private GameData gameData;
    private GameCore gameCore;
    /**
     * 格子宽度
     */
    private final int GRID_WIDTH = 25;

    GamePanel() {
        this.setSize(MAP_WIDTH*(GRID_WIDTH+1)+120,MAP_HEIGHT*(GRID_WIDTH+1)+20);
        this.setLayout(null);
        startBtn = new JButton("开始游戏");
        startBtn.setSize(110,50);
        startBtn.setFont(new Font("", Font.BOLD, 15));
        startBtn.setLocation(MAP_WIDTH*(GRID_WIDTH+1)-10,100);
        startBtn.setMargin(new java.awt.Insets(0,0,0,0));
        startBtn.addActionListener(e->{
            gameCore.startGame();
        });
        this.add(startBtn);

        speedUpBtn = new JButton("加速");
        speedUpBtn.setSize(50,30);
        speedUpBtn.setFont(new Font("", Font.BOLD, 14));
        speedUpBtn.setMargin(new java.awt.Insets(0,0,0,0));

        speedUpBtn.setLocation(MAP_WIDTH*(GRID_WIDTH+1)-10,160);
        this.add(speedUpBtn);

        speedDownBtn = new JButton("减速");
        speedDownBtn.setSize(50,30);
        speedDownBtn.setFont(new Font("", Font.BOLD, 14));
        speedDownBtn.setMargin(new java.awt.Insets(0,0,0,0));
        speedDownBtn.setLocation(MAP_WIDTH*(GRID_WIDTH+1)+50,160);
        this.add(speedDownBtn);

        init();
    }
    private void init(){
        gameData = new GameData();
        gameCore = new GameCore(gameData, this);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //绘制地图格子
        for(int i=0;i<=MAP_WIDTH;i++){
            g.drawLine(0,i*GRID_WIDTH,MAP_WIDTH*GRID_WIDTH,i*GRID_WIDTH);
            g.drawLine(i*GRID_WIDTH,0,i*GRID_WIDTH,MAP_HEIGHT*GRID_WIDTH);
        }
        //绘制地图
        for(int i=0;i<MAP_WIDTH;i++){
            for(int j=0;j<MAP_HEIGHT;j++){
                MapEnum type = gameData.getPoint(i,j);
                if(type != EMPTY){
                    if(type == WALL){
                        g.setColor(Color.YELLOW);
                    }else if(type == SNAKE_BODY){
                        g.setColor(Color.BLUE);
                    }else if(type == SNAKE_HEAD){
                        g.setColor(Color.RED);
                    }if(type == FOOD){
                        g.setColor(Color.GREEN);
                    }
                    g.fillOval(i*GRID_WIDTH,j*GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
                }

            }
        }
        //分数
        g.setColor(Color.black);
        g.setFont(Font.getFont("黑体"));
        g.drawString("当前分数:"+ gameData.getScore(),MAP_WIDTH*(GRID_WIDTH+1),50);
        g.drawString("游戏状态:"+gameData.getGameStatus().getContent(),MAP_WIDTH*(GRID_WIDTH+1),80);
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
    }
}
