package com.netanelad.netaneladgame;

import android.content.Context;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Missile extends AnimatedGameObject{
    private static final int MIN_SPEED = 7;
    private static final int MAX_SPEED = 40;
    private static final int SPEED_PER_SCORE_RATIO = 30;
    private static final int WIDTH_OFFSET_FOR_COLLISION = 10;
    private static final int MISSILE_WIDTH = 45;
    private static final int MISSILE_HEIGHT = 15;
    private static final int MISSILE_NUM_FRAMES = 13;
    private int speed;
    private Random rand = new Random();

    public Missile(Context context, int x, int y, int score) {
        super(x, y, 0, 0, MISSILE_WIDTH, MISSILE_HEIGHT,
                BitmapFactory.decodeResource(context.getResources(),R.drawable.missile),
                MISSILE_NUM_FRAMES,1);
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

    @Override
    public boolean removeWhenDead() {
        return true;
    }
}
