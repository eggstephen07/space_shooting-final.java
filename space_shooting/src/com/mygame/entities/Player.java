package com.mygame.entities;

import com.mygame.main.MainFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.List;

public class Player {

    private int x, y;
    private int dx, dy; 
    private final int SPEED = 5; 
    private final int SIZE = 30; 
    
    // --- HP 系統 ---
    private int maxHp = 100;
    private int currentHp = 100;
    private boolean isAlive = true;
    
    // 無敵時間
    private boolean isInvulnerable = false;
    private long invulnerabilityEndTime = 0;
    private final long INVULNERABILITY_DURATION = 500;
    
    // 射擊相關
    private boolean isShooting = false;
    private long lastShotTime = -2000;
    private final long SHOOT_COOLDOWN = 150; 
    
    // 【注意】這裡已經沒有 MAX_Y_TOP 變數了

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.dx = 0;
        this.dy = 0;
    }

    public void update() {
        // 無敵時間檢查
        if (isInvulnerable && System.currentTimeMillis() > invulnerabilityEndTime) {
            isInvulnerable = false;
        }

        // 1. 移動邏輯
        x += dx * SPEED;
        y += dy * SPEED;
        
        // 2. 邊界限制
        
        // 左邊界 (不變)
        if (x < 0) x = 0;
        // 右邊界 (使用新的 GAME_WIDTH)
        if (x > MainFrame.GAME_WIDTH - SIZE) x = MainFrame.GAME_WIDTH - SIZE;
        
        // 上邊界 (不變！) 
        // 因為 GamePanel 是放在標頭下方，所以 GamePanel 的 y=0 就是標頭的下緣。
        // 飛機 y=0 時，視覺上剛好就在標頭下方，不會飛進標頭裡。
        if (y < 0) y = 0; 
        
        // 下邊界 (使用新的 GAME_HEIGHT)
        // 確保飛機不會飛出遊戲面板的底部
        if (y > MainFrame.GAME_HEIGHT - SIZE) y = MainFrame.GAME_HEIGHT - SIZE;
    }

    // --- 自動射擊偵測邏輯 ---
    public void checkEnemyDetection(List<Enemy> enemies) {
        isShooting = false; 

        int myLeft = this.x;
        int myRight = this.x + SIZE;

        for (Enemy enemy : enemies) {
            if (enemy.isVisible()) {
                int enemyLeft = enemy.getX();
                int enemyRight = enemy.getX() + enemy.getWidth();

                boolean xOverlap = (myLeft < enemyRight) && (myRight > enemyLeft);
                boolean isAbove = enemy.getY() < this.y;

                if (xOverlap && isAbove) {
                    isShooting = true;
                    break; 
                }
            }
        }
    }

    // --- 鍵盤按下事件 ---
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) dx = -1; 
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) dx = 1; 
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) dy = -1;
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) dy = 1;
    }

    // --- 鍵盤釋放事件 ---
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && dx < 0) dx = 0;
        if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && dx > 0) dx = 0;
        if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && dy < 0) dy = 0;
        if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && dy > 0) dy = 0;
    }

    // --- 核心傷害方法 ---
    public void takeDamage(int damage) {
        if (!isInvulnerable) {
            currentHp -= damage;
            isInvulnerable = true;
            invulnerabilityEndTime = System.currentTimeMillis() + INVULNERABILITY_DURATION;

            if (currentHp <= 0) {
                currentHp = 0;
                isAlive = false;
            }
        }
    }

    // --- Getter 方法 ---
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public boolean isAlive() { return isAlive; }

    public void draw(Graphics g) {
        if (!isInvulnerable || (System.currentTimeMillis() % 200 < 100)) {
             g.setColor(Color.CYAN);
             g.fillRect(x, y, SIZE, SIZE);
        }
    }
    
    public boolean isShooting() {
        long currentTime = System.currentTimeMillis();
        if (isShooting && (currentTime - lastShotTime > SHOOT_COOLDOWN)) {
            lastShotTime = currentTime;
            return true;
        }
        return false;
    }
    
    public int getBulletStartX() { return x + (SIZE / 2); }
    public int getBulletStartY() { return y; }
    public java.awt.Rectangle getBounds() { return new java.awt.Rectangle(x, y, SIZE, SIZE); }
}
