package com.netanelad.netaneladgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


public class Background {

    private Bitmap image;
    private int x, y, dx;

    public Background(Context context) {
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.grassbg1);
        dx = GamePanel.MOVESPEED;
        x = 0;
        y = 0;
    }

    public void update () {
        x += dx;
        if (x <-GamePanel.WIDTH) {
            x = 0;
        }
    }

    public void draw (Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
        if (x <0) {
            canvas.drawBitmap(image, x +GamePanel.WIDTH, y, null);
        }
    }
}
