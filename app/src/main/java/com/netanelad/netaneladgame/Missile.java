package com.netanelad.netaneladgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by elad on 28/04/2017.
 */

public class Missile extends GameObject{
    private int m_score;
    private int m_speed;
    private Random m_ramd = new Random();
    private Animation m_animation = new Animation();
    private Bitmap m_spritesheet;

    public Missile(Bitmap res, int x, int y, int w, int h, int s, int numFrames) {
        m_x = x;
        m_y = y;
        m_width = w;
        m_height = h;
        m_score = s;
        m_speed = 7 + (int) (m_ramd.nextDouble()*m_score/30);
        // Cap missle speed
        if (m_speed > 40) m_speed = 40;

        Bitmap[] image = new Bitmap[numFrames];
        m_spritesheet = res;

        for (int i=0; i<image.length; i++) {
            image[i] = Bitmap.createBitmap(m_spritesheet, 0, i*m_height, m_width, m_height);
        }

        m_animation.setFrames(image);
        m_animation.setDelay(100-m_speed);
    }

    public void update() {
        m_x-=m_speed;
        m_animation.update();
    }

    public void draw (Canvas canvas) {
        try {
            canvas.drawBitmap(m_animation.getImage(), m_x, m_y, null);
        }
        catch (Exception e) {}
    }

    @Override
    public int getWidth() {
        // Offset slightly for more realistic collision detection
        return m_width-10;
    }
}
