package com.netanelad.netaneladgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;

public class Explosion extends AnimatedGameObject {
    private static final int IMAGES_PER_ROW = 5;
    private Context context;

    public Explosion (Context context, Bitmap res, int x, int y, int w, int h, int numFrames) {
        super(x,y,0,0,w,h,res,numFrames,IMAGES_PER_ROW);
        this.context = context;
        //MediaPlayer.create( getApplicationContext(), R.raw.explosion).start();
    }

    @Override
    public void draw (Canvas canvas) {
        if (!animation.playedOnce()) {
            super.draw(canvas);
            Sounds sounds = new Sounds();
            sounds.play_explosion_sound(this.context);
        }
    }

    @Override
    public void update () {
        if (!animation.playedOnce()) {
            super.update();


        }
    }

    @Override
    public boolean isColidable() {
        return false;
    }

    public void playSound(){
        MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(context, R.raw.explosion);
        mediaPlayer.start();

    }


}
