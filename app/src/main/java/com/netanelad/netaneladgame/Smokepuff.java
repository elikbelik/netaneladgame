package com.netanelad.netaneladgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by elad on 28/04/2017.
 */

public class Smokepuff extends GameObject{
    public int m_r;

    public Smokepuff(int x, int y) {
        m_r = 5;
        super.m_x = x;
        super.m_y = y;
    }

    public void draw (Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(m_x-m_r, m_y-m_r, m_r, paint);
        canvas.drawCircle(m_x-m_r+2, m_y-m_r-2, m_r, paint);
        canvas.drawCircle(m_x-m_r+4, m_y-m_r+1, m_r, paint);
    }

    public void update() {
        m_x -= 10;
    }
}
