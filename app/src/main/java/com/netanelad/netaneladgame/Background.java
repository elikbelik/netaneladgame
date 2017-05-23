package com.netanelad.netaneladgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Background {

    private Bitmap m_image;
    private int m_x, m_y, m_dx;

    public Background(Bitmap res) {
        m_image = res;
        m_dx = GamePanel.MOVESPEED;
        m_x = 0;
        m_y = 0;
    }

    public void update () {
        m_x += m_dx;
        if (m_x<-GamePanel.WIDTH) {
            m_x = 0;
        }
    }

    public void draw (Canvas canvas) {
        canvas.drawBitmap(m_image, m_x, m_y, null);
        if (m_x<0) {
            canvas.drawBitmap(m_image, m_x+GamePanel.WIDTH, m_y, null);
        }
    }
}
