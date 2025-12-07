package com.mygame.managers;

public class ScoreManager {
    
    private int score = 0;
    private int currency = 0; // 新增貨幣變數
    private final int ENEMY_SCORE_VALUE = 10; 

    // --- 分數相關 ---
    public void addScore() {
        score += ENEMY_SCORE_VALUE;
    }

    public int getScore() {
        return score;
    }

    // --- 【關鍵修正】 這些是錯誤訊息中說缺少的貨幣方法 ---
    
    public void addCurrency(int value) {
        currency += value;
    }
    
    public int getCurrency() {
        return currency;
    }
    
    public boolean spendCurrency(int cost) {
        if (currency >= cost) {
            currency -= cost;
            return true;
        }
        return false;
    }

    public void reset() {
        score = 0;
        currency = 0;
    }
}
