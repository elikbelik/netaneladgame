package com.netanelad.netaneladgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Explosion extends AnimatedGameObject {
    private static final int IMAGES_PER_ROW = 5;

    public Explosion (Bitmap res, int x, int y, int w, int h, int numFrames) {
        super(x,y,0,0,w,h,res,numFrames,IMAGES_PER_ROW);
    }

    @Override
    public void draw (Canvas canvas) {
        if (!animation.playedOnce()) {
            super.draw(canvas);
        }
    }

    @Override
    public void update () {
        if (!animation.playedOnce()) {
            super.update();
        }
    }
}
