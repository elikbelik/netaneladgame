package com.netanelad.netaneladgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

public class Border extends GameObject{
    public enum BorderType {Top, Bottom};
    public static final int BORDER_WIDTH = 20;
    private static final int BORDER_HEIGHT = 200;
    // Increase to slow down difficulty progression, decrease to speed up difficulty progression
    private static final int PROGRESS_DENUM = 20;
    private static final int MIN_MAX_HEIGHT = 60;
    private static final int MIN_MIN_HEIGHT = 5;
    private static int score = 0;
    private static int lastHeight = 0;
    private static int movementDirection = 1;
    private static Map<BorderType, Integer> counter = new HashMap<BorderType, Integer>();
    private Bitmap m_image;
    private BorderType borderType;

    public Border(Context context, int x, int h, BorderType t) {
        super(x, (t==BorderType.Bottom) ? GamePanel.HEIGHT-h : h-BORDER_HEIGHT,
                GamePanel.MOVESPEED, 0, BORDER_WIDTH, BORDER_HEIGHT,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.brick));
        borderType = t;

        // Count how many object from the same type has been created
        if (!counter.containsKey(borderType))
            counter.put(borderType, 1);
        else
            counter.put(borderType, counter.get(borderType)+1);

        lastHeight = h;
    }

    public void update() {
        x += dx;
        // Borders reset themselves
        if (x+width < 0) {
            x += width*counter.get(borderType);
            lastHeight += movementDirection;
            y = ((borderType==BorderType.Bottom) ? GamePanel.HEIGHT-lastHeight : lastHeight-BORDER_HEIGHT);
            int maxBorderHeight = Math.min(BORDER_HEIGHT, MIN_MAX_HEIGHT + score/PROGRESS_DENUM);
            int minBorderHeight = MIN_MIN_HEIGHT+ score/PROGRESS_DENUM;
            // Replace direction if needed
            if (lastHeight>=maxBorderHeight)
                movementDirection = -1;
            else if (lastHeight<=minBorderHeight)
                movementDirection = 1;
        }
    }

    @Override
    public boolean removeWhenDead() {
        return true;
    }

    public static void resetBordersCounter () {
        for (BorderType k : counter.keySet()) {
            counter.put(k,0);
        }
        score = 0;
    }

    public static void updateScore (int s) {
        score = s;
    }
}
