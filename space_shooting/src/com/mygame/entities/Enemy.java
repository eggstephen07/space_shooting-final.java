package com.mygame.entities;

import com.mygame.main.MainFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Enemy {

    private int x, y;
    private final int SPEED = 2;
    private final int SIZE = 40;
    private boolean visible;
    
    // 晶片掉落價值
    private final int SHARD_VALUE = 5; 
    
    private static final Random random = new Random(); 

    public Enemy() {
        this.visible = true;
        this.y = -SIZE; 
        this.x = random.nextInt(MainFrame.GAME_WIDTH - SIZE); 
    }

    public void update() {
        y += SPEED;
        if (y > MainFrame.GAME_HEIGHT) {
            visible = false;
        }
    }

    public void draw(Graphics g) {
        if (visible) {
            g.setColor(Color.RED);
            g.fillRect(x, y, SIZE, SIZE);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    // --- 【關鍵修正】 這些是錯誤訊息中說缺少的 getter 方法 ---
    
    // 1. 用於晶片生成位置
    public int getCenterX() { return x + (SIZE / 2); }
    public int getCenterY() { return y + (SIZE / 2); }
    public int getShardValue() { return SHARD_VALUE; }

    // 2. 用於自動射擊偵測
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return SIZE; }
}
