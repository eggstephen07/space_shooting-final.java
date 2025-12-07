package com.mygame.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 650; 
    
    // 定義區域高度
    public static final int HEADER_HEIGHT = 50;
    public static final int GAME_HEIGHT = WINDOW_HEIGHT - HEADER_HEIGHT - 40; 
    public static final int GAME_WIDTH = WINDOW_WIDTH - 16; 

    public MainFrame() {
        setTitle("Java Space Shooter");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        setLayout(new BorderLayout());

        // --- 標頭 ---
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, HEADER_HEIGHT));
        headerPanel.setBackground(Color.DARK_GRAY);
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("GALAXY SHOOTER");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // 讓標頭 "不要" 搶焦點 (這行很重要)
        headerPanel.setFocusable(false); 
        titleLabel.setFocusable(false);

        this.add(headerPanel, BorderLayout.NORTH);
        
        // --- 遊戲面板 ---
        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel, BorderLayout.CENTER);
        
        // 1. 先顯示視窗
        setVisible(true);
        
        // 2. 【關鍵修正】 視窗顯示後，強迫遊戲面板獲取焦點
        // 如果這行寫在 setVisible 之前，會失敗！
        gamePanel.requestFocusInWindow(); 
    }
}
