package com.netanelad.netaneladgame;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by netan on 5/29/2017.
 */

public class Sounds {
    private SoundPool soundPool;
    private int explosionId;
    private AudioManager audioManager;

    public Sounds (Context context) {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
        explosionId = soundPool.load(context, R.raw.explosion, 1);
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void play_explosion_sound(){
        float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float leftVolume = curVolume/maxVolume;
        float rightVolume = curVolume/maxVolume;
        int priority = 1;
        int no_loop = 0;
        float normal_playback_rate = 1f;
        soundPool.play(explosionId, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
    }
}
