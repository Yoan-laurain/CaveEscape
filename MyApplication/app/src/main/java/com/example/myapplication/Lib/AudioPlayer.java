package com.example.myapplication.Lib;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.example.myapplication.R;

public class AudioPlayer
{
    private static MediaPlayer ring;

    public AudioPlayer ( Context myContext )
    {
        this.ring = MediaPlayer.create(myContext, R.raw.mainmusic);
        Play(myContext);
    }

    public static MediaPlayer getRing() {
        return ring;
    }

    public void Play( Context myContext )
    {
        AudioManager manager = (AudioManager)myContext.getSystemService(Context.AUDIO_SERVICE);
        if(!manager.isMusicActive())
        {
            ring = MediaPlayer.create(myContext, R.raw.mainmusic);
            ring.setLooping(true);
            ring.start();
        }
    }


}
