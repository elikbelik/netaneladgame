package com.netanelad.netaneladgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

public class TopBorder extends GameObject{
    public enum BorderType {Top, Bottom};
    public static final int BORDER_WIDTH = 20;
    private static final int BORDER_HEIGHT = 200;
    // Increase to slow down difficulty progression, decrease to speed up difficulty progression
    private static final int PROGRESS_DENUM = 20;
    private static final int MIN_MAX_HEIGHT = 30;
    private static final int MIN_MIN_HEIGHT = 5;
    private static int score = 0;
    private static int lastHeight = 0;
    private static int movementDirection = 1;
    private static Map<BorderType, Integer> counter = new HashMap<BorderType, Integer>();
    private Bitmap m_image;
    private BorderType borderType;

    public TopBorder (Context context, int x, int h, BorderType t) {
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

    // TODO: Every 50 points insert randomly placed top blocks that break the pattern
    // TODO: Every 40 points insert randomly placed bot blocks that break the pattern
    /* Orig data:
    if (player.getScore()%50 == 0) {
        m_topBorder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
            m_topBorder.get(m_topBorder.size()-1).getX()+20,0,(int)(m_rand.nextDouble()*m_maxBorderHeight)+1));

                    // Every 40 points insert randomly placed top blocks that break the pattern
        if (player.getScore()%40 == 0) {
            m_botBorder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                    m_botBorder.get(m_botBorder.size()-1).getX()+20,(int)(m_rand.nextDouble()*m_maxBorderHeight)+HEIGHT-m_maxBorderHeight));
        }
        for (int i=0; i<m_botBorder.size(); i++) {
            m_botBorder.get(i).update();
            if (m_botBorder.get(i).getX()<-20) {
                // Remove element of arraylist, replace it by adding a new one
                m_botBorder.remove(i);
                i--;

    */



}
