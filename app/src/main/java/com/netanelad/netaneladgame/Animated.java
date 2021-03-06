package com.netanelad.netaneladgame;


import android.graphics.Bitmap;

public class Animated {

    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;

    public void setFrames (Bitmap[] frames) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void setDelay (long b) {
        delay = b;}
    public void setFrame (int i) {
        currentFrame = i;}

    public void update () {
        long elapsed = (System.nanoTime()- startTime)/MainThread.SEC_TO_MILI;

        if (elapsed> delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }

        if (currentFrame == frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    }

    public Bitmap getImage() {
        return frames[currentFrame];
    }

    public int getFrame() {return currentFrame;}
    public boolean playedOnce() {return playedOnce;}
}
