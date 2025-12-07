package com.mygame.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet {

    private int x, y;
    private final int SPEED = 10;
    private final int WIDTH = 3;
    private final int HEIGHT = 10;
    private boolean visible;

    public Bullet(int startX, int startY) {
        this.x = startX - (WIDTH / 2); 
        this.y = startY;
        this.visible = true;
    }

    public void update() {
        y -= SPEED;

        if (y < 0) {
            visible = false;
        }
    }

    public void draw(Graphics g) {
        if (visible) {
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
