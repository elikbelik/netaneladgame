package com.netanelad.netaneladgame;

import android.graphics.Rect;

public abstract class GameObject {
    protected int m_x;
    protected int m_y;
    protected int m_dx;
    protected int m_dy;
    protected int m_width;
    protected int m_height;

    public void setX (int x) {m_x = x;}
    public void setY (int y) {m_y = y;}
    public int getX () {return m_x;}
    public int getY () {return m_y;}
    public int getHeight () {return m_height;}
    public int getWidth () {return m_width;}

    public Rect getRectangle () {
        return new Rect(m_x, m_y, m_x+m_width, m_y+m_height);
    }
}
