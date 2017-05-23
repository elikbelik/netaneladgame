package com.netanelad.netaneladgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by elad on 28/04/2017.
 */

public class Missile extends AnimatedGameObject{
    private static final int MIN_SPEED = 7;
    private static final int MAX_SPEED = 40;
    private static final int SPEED_PER_SCORE_RATIO = 30;
    private static final int WIDTH_OFFSET_FOR_COLLISION = 10;
    private int speed;
    private Random rand = new Random();

    public Missile(Bitmap res, int x, int y, int w, int h, int score, int numFrames) {
        super(x,y,0,0,w,h,res,numFrames,1);
        speed = MIN_SPEED + (int) (rand.nextDouble()* score /SPEED_PER_SCORE_RATIO);
        // Cap missle speed
        if (speed > MAX_SPEED) speed = MAX_SPEED;

        animation.setDelay(100- speed);
    }

    public void update() {
        x -= speed;
        super.update();
    }

    @Override
    public int getWidth() {
        // Offset slightly for more realistic collision detection
        return width - WIDTH_OFFSET_FOR_COLLISION;
    }
}
