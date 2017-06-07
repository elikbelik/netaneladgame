package com.netanelad.netaneladgame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;

public class Explosion extends AnimatedGameObject {
    public static final int EXPLOSION_WIDTH = 100;
    public static final int EXPLOSION_HEIGHT = 100;
    private static final int EXPLOSION_NUM_FRAMES = 25;
    private static final int IMAGES_PER_ROW = 5;
    private Context context;

    public Explosion (Context context, int x, int y) {
        super(x,y,0,0,EXPLOSION_WIDTH ,EXPLOSION_HEIGHT, BitmapFactory.decodeResource(context.getResources(),
                R.drawable.explosion),EXPLOSION_NUM_FRAMES,IMAGES_PER_ROW);
        this.context = context;
        //MediaPlayer.create( getApplicationContext(), R.raw.explosion).start();
        Sounds sounds = new Sounds();
        sounds.play_explosion_sound(this.context);
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

    @Override
    public boolean isColidable() {
        return false;
    }

    public void playSound(){
        MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(context, R.raw.explosion);
        mediaPlayer.start();

    }

    @Override
    public boolean removeWhenDead() {
        return true;
    }
}
