package com.netanelad.netaneladgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

public class TopBorder extends GameObject{
    public enum BorderType {Top, Bottom};
    public static final int BORDER_WIDTH = 20;
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
    private Context context;

    public TopBorder (Context context, int x, int y, int h, BorderType t) {
        super(x, y, GamePanel.MOVESPEED, 0, BORDER_WIDTH,h,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.brick));
        borderType = t;
        this.context = context;

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
            height = lastHeight + movementDirection;
            lastHeight = height;
            image = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.brick), 0, 0, width, height);
            int maxBorderHeight = Math.min(GamePanel.HEIGHT/4, MIN_MAX_HEIGHT + score/PROGRESS_DENUM);
            int minBorderHeight = MIN_MIN_HEIGHT+ score/PROGRESS_DENUM;
            // Replace direction if needed
            if (height>=maxBorderHeight)
                movementDirection = -1;
            else if (height<=minBorderHeight)
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
    */
    // TODO: Calculate topdown
    /*
    // Calculate topdown which determines the direction the border is moving (up or down)
                if (m_topBorder.get(m_topBorder.size()-1).getHeight() >= m_maxBorderHeight) {
        m_topDown = false;
    }
                if (m_topBorder.get(m_topBorder.size()-1).getHeight()<=m_minBorderHeight) {
        m_topDown = true;
    }
    if (m_topDown) {
                    m_topBorder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                            m_topBorder.get(m_topBorder.size()-1).getX()+20, 0, m_topBorder.get(m_topBorder.size()-1).getHeight()+1));
                }
                // New border added will have smaller height
                else {
                    m_topBorder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                            m_topBorder.get(m_topBorder.size()-1).getX()+20, 0, m_topBorder.get(m_topBorder.size()-1).getHeight()-1));
                }
    */
    // TODO: Set the initial borders heights
    /*
    if (i==0) {
                m_topBorder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                        i * 20, 0, 10));
                m_botBorder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                        i*20, HEIGHT-m_minBorderHeight));
            }
            else {
                m_topBorder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                        i * 20, 0, m_topBorder.get(i-1).getHeight()+1));
                m_botBorder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                        i*20, m_botBorder.get(i-1).getY()-1));
            }
     */
    // TODO: set the height to decrease with the score
    /*
    // Calculate the threshold of height the border can have based on the score
            // Max and min border heart are updated, and the border switched direction when either max or min is met
            m_maxBorderHeight = 30+ player.getScore()/m_progressDenom;
            // Cap max border height so that borders can only take up a total of 1/2 the screen
            if (m_maxBorderHeight > HEIGHT/4) m_maxBorderHeight = HEIGHT/4;
            m_minBorderHeight = 5+ player.getScore()/m_progressDenom;
     */
}
