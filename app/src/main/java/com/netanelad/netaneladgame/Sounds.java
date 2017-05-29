package com.netanelad.netaneladgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

/**
 * Created by netan on 5/29/2017.
 */

public class Sounds {
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;
    private MediaPlayer mediaPlayer;
    private Context context;

    public void play_explosion_sound(Context context){
        this.context = context;

        mediaPlayer= MediaPlayer.create(context, R.raw.explosion);
        mediaPlayer.start();
        playedOnce = true;
        startTime = System.nanoTime();
    }

    public void setDelay (long b) {
        delay = b;}

    public void update () {
        long elapsed = (System.nanoTime()- startTime)/MainThread.SEC_TO_MILI;

        if (elapsed> delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
    }

    public boolean playedOnce() {return playedOnce;}
}
