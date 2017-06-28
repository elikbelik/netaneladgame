package com.netanelad.netaneladgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Player extends AnimatedGameObject {
    private static final int X_POSITION = 100;
    private static final int MIN_TIME_INTERVAL_FOR_SCORE = 100;
    private static final int DY_INCREASE_RATE = 1;
    private static final int MIN_DY = -14;
    private static final int MAX_DY = 14;
    private static final double ACCELERATION = 1.5;
    private int score;
    private boolean isUp;
    private boolean isPlaying;
    private long startTime;

    public Player(Context context, int w, int h, int numFrames) {
        super(X_POSITION,GamePanel.HEIGHT/2, 0, 0, w, h,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.helicopter),
                numFrames, numFrames);
        score = 0;
        startTime = System.nanoTime();
    }

    public void setUp (boolean b) {
        isUp = b;
    }

    public void update() {
        long elapsed = (System.nanoTime()- startTime)/MainThread.SEC_TO_MILI;
        if (elapsed> MIN_TIME_INTERVAL_FOR_SCORE) {
            score++;
            startTime = System.nanoTime();
        }
        super.update();

        if (isUp) {
            dy -= DY_INCREASE_RATE;
        }
        else {
            dy += DY_INCREASE_RATE;
        }

        if (dy >MAX_DY) dy = MAX_DY;
        if (dy <MIN_DY) dy = MIN_DY;

        y += (int) dy *ACCELERATION;
        if (y < 0) {
            y = 0;
            resetDy();
        }
        if (y > GamePanel.HEIGHT- height) {
            y = GamePanel.HEIGHT- height;
            resetDy();
        }
    }

    public int getScore() {return score;}
    public boolean getPlaying() {return isPlaying;}
    public void setPlaying (boolean b) {isPlaying = b;}
    public void resetDy() {dy = 0;}
    public void resetScore() {score = 0;}
}
