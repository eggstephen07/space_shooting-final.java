package com.mygame.main;

public class Game {
    public static void main(String[] args) {
        // 確保在事件派發執行緒中啟動 MainFrame
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}
