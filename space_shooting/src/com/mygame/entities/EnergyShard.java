package com.mygame.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class EnergyShard {

    private int x, y;
    private final int SPEED = 2; // (選用) 我也稍微把速度從 1 改成 2，讓它掉落稍微有感一點
    
    // 【修改點】 將尺寸從 8 改大到 15，讓判定範圍變大，更好撿
    private final int SIZE = 15;  
    
    private int value;
    private boolean visible;

    public EnergyShard(int startX, int startY, int value) {
        this.x = startX - (SIZE / 2); 
        this.y = startY - (SIZE / 2);
        this.value = value;
        this.visible = true;
    }

    public void update() {
        y += SPEED;
        // 邊界檢查 (稍微寬鬆一點，讓它完全離開畫面再消失)
        if (y > com.mygame.main.MainFrame.WINDOW_HEIGHT) {
            visible = false; 
        }
    }

    public void draw(Graphics g) {
        if (visible) {
            // 您也可以改顏色，例如改成黃色 (YELLOW) 或洋紅色 (MAGENTA) 更顯眼
            g.setColor(Color.ORANGE); 
            g.fillOval(x, y, SIZE, SIZE);
            
            // (選用) 加個白色邊框讓它更像道具
            g.setColor(Color.WHITE);
            g.drawOval(x, y, SIZE, SIZE);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public boolean isVisible() { return visible; }
    public int getValue() { return value; }
    public void setVisible(boolean visible) { this.visible = visible; }
}
