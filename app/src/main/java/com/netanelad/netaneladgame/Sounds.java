package com.netanelad.netaneladgame;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by netan on 5/29/2017.
 */

public class Sounds {

    public static void play_explosion_sound(Context context){
        MediaPlayer mp = MediaPlayer.create(context, R.raw.explosion);
        mp.start();
    }
}
