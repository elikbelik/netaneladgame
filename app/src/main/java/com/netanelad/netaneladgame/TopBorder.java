package com.netanelad.netaneladgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by elad on 28/04/2017.
 */

public class TopBorder extends GameObject{

    private Bitmap m_image;

    public TopBorder (Bitmap res, int x, int y, int h) {
        m_height = h;
        m_width = 20;
        m_x = x;
        m_y = y;
        m_dx = GamePanel.MOVESPEED;
        m_image = Bitmap.createBitmap(res, 0, 0, m_width, m_height);
    }

    public void update() {
        m_x+=m_dx;
    }

    public void draw (Canvas canvas) {
        try {
            canvas.drawBitmap(m_image, m_x, m_y, null);
        }
        catch (Exception e) {}
    }

}
