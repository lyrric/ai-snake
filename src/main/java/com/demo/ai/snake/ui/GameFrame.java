package com.demo.ai.snake.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2018/12/18.
 *
 * @author wangxiaodong
 */
public class GameFrame extends JFrame {

    private GamePanel gamePanel;

    public GameFrame() throws HeadlessException {
        this.setTitle("AI-snake");
        this.setSize(525+120,525+20);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        gamePanel = new GamePanel();
        this.add(gamePanel);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void update(Graphics g) {
        super.update(g);

    }


}
