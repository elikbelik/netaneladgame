package com.netanelad.netaneladgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by elad on 28/04/2017.
 */

public class Smokepuff extends GameObject{
    public static final int RADIUS = 5;
    public static final int MOVESPEED = 10;
    public static final int CIRCLE_2_OFFSET_X = 2;
    public static final int CIRCLE_2_OFFSET_Y = -2;
    public static final int CIRCLE_3_OFFSET_X = 4;
    public static final int CIRCLE_3_OFFSET_Y = 1;

    public Smokepuff(int x, int y) {
        super(x,y,0,0,0,0,null);
    }

    public void draw (Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(x - RADIUS, y - RADIUS, RADIUS, paint);
        canvas.drawCircle(x - RADIUS + CIRCLE_2_OFFSET_X, y - RADIUS + CIRCLE_2_OFFSET_Y, RADIUS, paint);
        canvas.drawCircle(x - RADIUS + CIRCLE_3_OFFSET_X, y - RADIUS + CIRCLE_3_OFFSET_Y, RADIUS, paint);
    }

    public void update() {
        x -= MOVESPEED;
    }
}
