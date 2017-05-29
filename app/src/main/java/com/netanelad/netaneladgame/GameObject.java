package com.netanelad.netaneladgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;
    protected Bitmap image;

    public GameObject (int x, int y, int dx, int dy, int w, int h, Bitmap res) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        width = w;
        height = h;
        if (res != null)
            image = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    public void setX (int x) {this.x = x;}
    public void setY (int y) {this.y = y;}
    public int getX () {return x;}
    public int getY () {return y;}
    public int getHeight () {return height;}
    public int getWidth () {return width;}

    public Rect getRectangle () {
        return new Rect(x, y, x + width, y + height);
    }

    public abstract void update();

    public void draw (Canvas canvas) {
        try {
            if (image != null)
                canvas.drawBitmap(image, x, y, null);
        }
        catch (Exception e) {}
    }

    public boolean isColidable () { return true; }

    public boolean shouldRemove () {
        return y+width < 0;
    }

    public boolean removeWhenDead () { return false; }

    public boolean hideWhenDead () { return false; }
}
