package com.netanelad.netaneladgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.AlphaAnimation;

import java.util.ArrayList;
import java.util.Random;


public class Background {

    final static int[] POSSIBLE_BACKGROUNDS = {R.drawable.bg1, R.drawable.bg2, R.drawable.bg3};
    final static int TIME_TO_CHANGE_BACKGROUND = 10000;
    final static int STEP_ALPHA = 10;
    private Bitmap image;
    private ArrayList<Bitmap> backImages;
    private int x, y, dx;
    private int currentBackgroundIndex;
    private int previousBackgroundIndex;
    private Random rand = new Random();
    private long backStartTime;
    private Paint backPaint;
    private int alpha;


    public Background(Context context) {
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        dx = GamePanel.MOVESPEED;
        x = 0;
        y = 0;
        currentBackgroundIndex = rand.nextInt(POSSIBLE_BACKGROUNDS.length);
        previousBackgroundIndex = currentBackgroundIndex;
        backStartTime = System.nanoTime();
        backPaint = new Paint();
        alpha = 0;

        // Init the background images
        backImages = new ArrayList<Bitmap>();
        for (int back : POSSIBLE_BACKGROUNDS ) {
            backImages.add(BitmapFactory.decodeResource(context.getResources(), back));
        }
    }

    public void update () {
        x += dx;
        if (x <-GamePanel.WIDTH) {
            x = 0;
        }
    }

    public void draw (Canvas canvas) {
        canvas.drawBitmap(backImages.get(currentBackgroundIndex), 0, 0, null);
        if (alpha > 0) {
            backPaint.setAlpha(alpha);
            canvas.drawBitmap(backImages.get(previousBackgroundIndex), 0, 0, backPaint);
            alpha -= STEP_ALPHA;
        }

        canvas.drawBitmap(image, x, y, null);
        if (x <0) {
            canvas.drawBitmap(image, x +GamePanel.WIDTH, y, null);
        }

        // Check whether change the background
        long elapsed = (System.nanoTime() - backStartTime)/ MainThread.SEC_TO_MILI;
        if (elapsed > TIME_TO_CHANGE_BACKGROUND) {
            previousBackgroundIndex = currentBackgroundIndex;
            currentBackgroundIndex = rand.nextInt(POSSIBLE_BACKGROUNDS.length);
            backStartTime = System.nanoTime();
            alpha = 255;
        }
    }
}
