package com.mygame.main;

import com.mygame.entities.Bullet;
import com.mygame.entities.Enemy;
import com.mygame.entities.EnergyShard;
import com.mygame.entities.Player;
import com.mygame.managers.ScoreManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    
    private final int DELAY = 16; 
    private Timer timer;
    
    private Player player; 
    private ArrayList<Bullet> bullets; 
    private ArrayList<Enemy> enemies; 
    private ArrayList<EnergyShard> shards;
    private ScoreManager scoreManager;
    
    // 敵機生成間隔控制
    private long lastEnemySpawnTime = 0;
    private final long ENEMY_SPAWN_INTERVAL = 1000; // 每 1 秒生成一架
    
    public GamePanel() {
        setPreferredSize(new Dimension(MainFrame.GAME_WIDTH, MainFrame.GAME_HEIGHT));
        
        setBackground(Color.BLACK);
        
        addKeyListener(new TAdapter());
        setFocusable(true);
        
        initGame();
    }
    
    private void initGame() {
        player = new Player(MainFrame.GAME_WIDTH / 2 - 15, MainFrame.GAME_HEIGHT - 50); 
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        shards = new ArrayList<>();
        scoreManager = new ScoreManager(); 
        
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame(); 
        repaint();
    }
    
    private void updateGame() {
        // 1. 更新玩家
        player.update();
        
        // --- 自動射擊偵測 (將敵機列表傳給玩家) ---
        player.checkEnemyDetection(enemies); 
        
        // 2. 處理玩家射擊
        if (player.isShooting()) {
            bullets.add(new Bullet(player.getBulletStartX(), player.getBulletStartY()));
        }

        // --- 3. 敵機生成邏輯 (您之前可能遺失了這段) ---
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastEnemySpawnTime > ENEMY_SPAWN_INTERVAL) {
            enemies.add(new Enemy());
            lastEnemySpawnTime = currentTime;
        }

        // 4. 更新子彈
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();
            if (!bullet.isVisible()) {
                bulletIterator.remove();
            }
        }
        
        // 5. 更新敵機
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.update();
            if (!enemy.isVisible()) { 
                enemyIterator.remove();
            }
        }
        
        // 6. 更新晶片
        Iterator<EnergyShard> shardIterator = shards.iterator();
        while (shardIterator.hasNext()) {
            EnergyShard shard = shardIterator.next();
            shard.update();
            if (!shard.isVisible()) {
                shardIterator.remove();
            }
        }
        
        // 7. 碰撞檢測
        checkCollisions(); 
    }
    
    private void checkCollisions() {
        // --- 子彈 vs 敵機 ---
        for (Bullet bullet : bullets) {
            if (!bullet.isVisible()) continue;

            for (Enemy enemy : enemies) {
                if (!enemy.isVisible()) continue;

                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    // 生成晶片
                    shards.add(new EnergyShard(enemy.getCenterX(), enemy.getCenterY(), enemy.getShardValue()));
                    
                    // 銷毀兩者
                    enemy.setVisible(false);
                    bullet.setVisible(false);
                    
                    scoreManager.addScore();
                    break; // 子彈已消失，跳出內層迴圈
                }
            }
        }
        
        // --- 玩家 vs 敵機 ---
        final int ENEMY_COLLISION_DAMAGE = 20; 
        for (Enemy enemy : enemies) {
            if (!enemy.isVisible()) continue;
            
            if (enemy.getBounds().intersects(player.getBounds())) {
                player.takeDamage(ENEMY_COLLISION_DAMAGE); 
                enemy.setVisible(false); 
                
                if (!player.isAlive()) {
                    timer.stop(); 
                }
            }
        }
        
        // --- 玩家 vs 晶片 ---
        for (EnergyShard shard : shards) {
            if (!shard.isVisible()) continue;
            
            if (shard.getBounds().intersects(player.getBounds())) {
                scoreManager.addCurrency(shard.getValue()); 
                shard.setVisible(false); 
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (player.isAlive()) {
            // 繪製物件
            player.draw(g);
            for (Bullet bullet : bullets) bullet.draw(g);
            for (Enemy enemy : enemies) enemy.draw(g);
            for (EnergyShard shard : shards) shard.draw(g);
            
            // 繪製 UI
            g.setColor(Color.WHITE);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
            g.drawString("Score: " + scoreManager.getScore(), MainFrame.GAME_WIDTH - 120, 20); 
            g.drawString("Energy: " + scoreManager.getCurrency(), MainFrame.GAME_WIDTH - 120, 45); 
            
            // 繪製 HP 數值
            g.drawString("HP: " + player.getCurrentHp() + " / " + player.getMaxHp(), 20, 20); 
            drawHpBar(g);

        } else {
            // 遊戲結束畫面
            g.setColor(Color.RED);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 40));
            String gameOverText = "GAME OVER!";
            int stringWidth = g.getFontMetrics().stringWidth(gameOverText);
            g.drawString(gameOverText, (MainFrame.GAME_WIDTH - stringWidth) / 2, MainFrame.GAME_HEIGHT / 2);
            
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
            String finalScoreText = "Final Score: " + scoreManager.getScore();
            int scoreWidth = g.getFontMetrics().stringWidth(finalScoreText);
            g.drawString(finalScoreText, (MainFrame.GAME_WIDTH - scoreWidth) / 2, MainFrame.GAME_HEIGHT / 2 + 50);
        }
        
        java.awt.Toolkit.getDefaultToolkit().sync();
    }
    
    private void drawHpBar(Graphics g) {
        int barWidth = 150;
        int barHeight = 10;
        int x = 20; 
        int y = 30; 

        double hpRatio = (double) player.getCurrentHp() / player.getMaxHp();
        int currentBarWidth = (int) (barWidth * hpRatio);

        g.setColor(Color.GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        if (hpRatio > 0.6) g.setColor(Color.GREEN);
        else if (hpRatio > 0.3) g.setColor(Color.YELLOW);
        else g.setColor(Color.RED);
        
        g.fillRect(x, y, currentBarWidth, barHeight);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, barWidth, barHeight);
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e); 
        }

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }
    }
    
}
