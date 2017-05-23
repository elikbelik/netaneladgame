package com.netanelad.netaneladgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class AnimatedGameObject extends GameObject {
    private static final int DELAY_TIME = 10;
    protected Bitmap spriteSheet;
    protected Animation animation = new Animation();

    public AnimatedGameObject(int x, int y, int dx, int dy, int w, int h, Bitmap res, int numFrames, int framesPerRow) {
        super(x,y,dx,dy,w,h,null);

        Bitmap[] image = new Bitmap[numFrames];
        spriteSheet = res;

        int row = 0;
        for (int i=0; i<image.length; i++) {
            if (i%framesPerRow==0 && i>0) row++;
            image[i] = Bitmap.createBitmap(spriteSheet, (i-framesPerRow*row)*width, row*height, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(DELAY_TIME);
    }

    public void update() {
        animation.update();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }
}
