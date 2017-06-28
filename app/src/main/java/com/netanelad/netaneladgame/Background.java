package com.netanelad.netaneladgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;


public class Background {

    final static int[] POSSIBLE_BACKGROUNDS = {R.drawable.bg1, R.drawable.bg2, R.drawable.bg3};
    private Bitmap image;
    private ArrayList<Bitmap> backImages;
    private int x, y, dx;
    private int currentBackgroundIndex;
    private Random rand = new Random();
    private long backStartTime;


    public Background(Context context) {
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        dx = GamePanel.MOVESPEED;
        x = 0;
        y = 0;
        currentBackgroundIndex = rand.nextInt(POSSIBLE_BACKGROUNDS.length);

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
        canvas.drawBitmap(image, x, y, null);
        if (x <0) {
            canvas.drawBitmap(image, x +GamePanel.WIDTH, y, null);
        }
    }
}
