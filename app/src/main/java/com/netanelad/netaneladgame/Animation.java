package com.netanelad.netaneladgame;


import android.graphics.Bitmap;

public class Animation {

    private Bitmap[] m_frames;
    private int m_currentFrame;
    private long m_startTime;
    private long m_delay;
    private boolean m_playedOnce;

    public void setFrames (Bitmap[] frames) {
        m_frames = frames;
        m_currentFrame = 0;
        m_startTime = System.nanoTime();
    }

    public void setDelay (long b) {m_delay = b;}
    public void setFrame (int i) {m_currentFrame = i;}

    public void update () {
        long elapsed = (System.nanoTime()-m_startTime)/1000000;

        if (elapsed>m_delay) {
            m_currentFrame++;
            m_startTime = System.nanoTime();
        }

        if (m_currentFrame == m_frames.length) {
            m_currentFrame = 0;
            m_playedOnce = true;
        }
    }

    public Bitmap getImage() {
        return m_frames[m_currentFrame];
    }

    public int getFrame() {return m_currentFrame;}
    public boolean playedOnce() {return m_playedOnce;}
}
