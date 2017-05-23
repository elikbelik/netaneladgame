package com.netanelad.netaneladgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends GameObject {
    private Bitmap m_spriteSheet;
    private int m_score;
    private boolean m_up;
    private boolean m_playing;
    private Animation m_animation = new Animation();
    private long m_startTime;

    public Player(Bitmap res, int w, int h, int numFrames) {
        m_x = 100;
        m_y = GamePanel.HEIGHT/2;
        m_dy = 0;
        m_score = 0;
        m_height = h;
        m_width = w;

        Bitmap[] image = new Bitmap[numFrames];
        m_spriteSheet = res;

        for (int i=0; i<image.length; i++) {
            image[i] = Bitmap.createBitmap(m_spriteSheet, i*m_width, 0, m_width, m_height);
        }

        m_animation.setFrames(image);
        m_animation.setDelay(10);
        m_startTime = System.nanoTime();
    }

    public void setUp (boolean b) {m_up = b;}

    public void update() {
        long elapsed = (System.nanoTime()-m_startTime)/1000000;
        if (elapsed>100) {
            m_score++;
            m_startTime = System.nanoTime();
        }
        m_animation.update();

        if (m_up) {
            m_dy -= 1;
        }
        else {
            m_dy += 1;
        }

        if (m_dy>14) m_dy = 14;
        if (m_dy<-14) m_dy = -14;

        m_y += m_dy*2;
        if (m_y < 0) {
            m_y = 0;
            resetDy();
        }
        if (m_y > GamePanel.HEIGHT-m_height) {
            m_y = GamePanel.HEIGHT-m_height;
            resetDy();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(m_animation.getImage(), m_x, m_y, null);
    }

    public int getScore() {return m_score;}
    public boolean getPlaying() {return m_playing;}
    public void setPlaying (boolean b) {m_playing = b;}
    public void resetDy() {m_dy = 0;}
    public void resetScore() {m_score = 0;}
}
