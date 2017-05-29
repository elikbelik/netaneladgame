package com.netanelad.netaneladgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Smokepuff extends GameObject{
    private static final int RADIUS = 5;
    private static final int MOVESPEED = 10;
    private static final int CIRCLE_2_OFFSET_X = 2;
    private static final int CIRCLE_2_OFFSET_Y = -2;
    private static final int CIRCLE_3_OFFSET_X = 4;
    private static final int CIRCLE_3_OFFSET_Y = 1;
    private static final int ABSTRACT_WIDTH = 1;

    public Smokepuff(int x, int y) {
        super(x,y,0,0,ABSTRACT_WIDTH,0,null);
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

    @Override
    public boolean isColidable() {
        return false;
    }

    @Override
    public boolean removeWhenDead() {
        return true;
    }

    @Override
    public boolean hideWhenDead() {
        return true;
    }
}
