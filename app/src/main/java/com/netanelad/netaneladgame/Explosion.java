package com.netanelad.netaneladgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Explosion {
    private int m_x;
    private int m_y;
    private int m_width;
    private int m_height;
    private int m_row;
    private Animation m_animation = new Animation();
    private Bitmap m_spritesheet;

    public Explosion (Bitmap res, int x, int y, int w, int h, int numFrames) {
        m_x = x;
        m_y = y;
        m_width = w;
        m_height = h;

        Bitmap[] image = new Bitmap[numFrames];
        m_spritesheet = res;

        for (int i=0; i<image.length; i++) {
            if (i%5==0 && i>0) m_row++;
            image[i] = Bitmap.createBitmap(m_spritesheet, (i-5*m_row)*m_width, m_row*m_height, m_width, m_height);
        }

        m_animation.setFrames(image);
        m_animation.setDelay(10);
    }

    public void draw (Canvas canvas) {
        if (!m_animation.playedOnce()) {
            canvas.drawBitmap(m_animation.getImage(),m_x,m_y,null);
        }
    }

    public void update () {
        if (!m_animation.playedOnce()) {
            m_animation.update();
        }
    }

    public int getHeight() {return m_height;}
}
