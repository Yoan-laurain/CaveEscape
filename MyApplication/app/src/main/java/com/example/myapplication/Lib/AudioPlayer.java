package com.example.myapplication.Lib;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.example.myapplication.R;

public class AudioPlayer
{
    private static MediaPlayer ring;
    private static int currentSong = 0;
    public static boolean isAnEasterEgg = false;
    private final static int MAX_VOLUME = 100;
    private static float current_volume;
    private static Context context;

    public static MediaPlayer getRing() {
        return ring;
    }

    public static float getCurrent_volume() { return current_volume; }

    public static void Play( Context myContext, int music )
    {
        context = myContext;
        AudioManager manager = (AudioManager)myContext.getSystemService(Context.AUDIO_SERVICE);

        if( !manager.isMusicActive() || ( music != currentSong && currentSong != 0 ) )
        {
            if ( manager.isMusicActive() )
            {
                ring.stop();
            }

            currentSong = music;
            ring = MediaPlayer.create(myContext, music);
            ring.setLooping(true);
            ring.start();
        }
        ChangeVolume((float) SharedPref.LoadVolumePreferences(myContext)) ;

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

    public static void ChangeVolume(float volume)
    {
        current_volume = volume;
        final float result = (float) (1 - (Math.log(MAX_VOLUME - volume) / Math.log(MAX_VOLUME)));
        ring.setVolume(result, result);
        SharedPref.SaveVolumePreferences( context,volume);

    }

}
