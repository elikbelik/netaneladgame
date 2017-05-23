package com.netanelad.netaneladgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by elad on 28/04/2017.
 */

public class BotBorder extends GameObject{
    private static final int WIDTH = 20;
    private static final int HEIGHT = 200;

    public BotBorder (Bitmap res, int x, int y) {
        super(x,y,GamePanel.MOVESPEED,0,WIDTH,HEIGHT,res);
    }

    public void update() {
        x += dx;
    }
}
