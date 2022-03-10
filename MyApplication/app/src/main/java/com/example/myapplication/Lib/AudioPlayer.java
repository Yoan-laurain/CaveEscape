package com.example.myapplication.Lib;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.example.myapplication.R;

public class AudioPlayer
{
    private static MediaPlayer ring;

    public static MediaPlayer getRing() {
        return ring;
    }

    public static void Play( Context myContext, int music )
    {
        AudioManager manager = (AudioManager)myContext.getSystemService(Context.AUDIO_SERVICE);
        if(!manager.isMusicActive())
        {
            ring = MediaPlayer.create(myContext, music);
            ring.setLooping(true);
            ring.start();
        }

    }

    public static void stop()
    {
        ring.pause();
        ring.stop();
    }

    public static void Resume()
    {
        ring.start();
    }

}
