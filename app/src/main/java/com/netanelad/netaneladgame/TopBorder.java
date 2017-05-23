package com.netanelad.netaneladgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by elad on 28/04/2017.
 */

public class TopBorder extends GameObject{
    private static final int WIDTH = 20;
    private Bitmap m_image;

    public TopBorder (Bitmap res, int x, int y, int h) {
        super(x,y,GamePanel.MOVESPEED,0,WIDTH,h,res);
    }

    public void update() {
        x += dx;
    }

}
