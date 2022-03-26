package com.example.myapplication.Lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class AudioPlayer
{
    //---------------------------------------

    private static MediaPlayer ring;
    private static int currentSong = 0;
    public static boolean isAnEasterEgg = false;
    private final static int MAX_VOLUME = 100;
    private static float current_volume;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    //---------------------------------------

    public static MediaPlayer getRing() {
        return ring;
    }

    public static float getCurrent_volume() { return current_volume; }

    //---------------------------------------

    /*
        Start the music in parameter and stop the current one if she exist
        if the current music is the same as the one in parameter we do nothing
        Set the volume to the level set in the cache
     */
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
        ChangeVolume(SharedPref.LoadVolumePreferences(myContext)) ;
    }

    /*
        Stop the current music
     */
    public static void Stop()
    {
        ring.pause();
        ring.stop();
    }

    /*
        Resume the current music
     */
    public static void Resume()
    {
         ring.start();
    }

    /*
        Change the volume of the music and save the volume level in the cache to reload it
        on the next launch of the app
     */
    public static void ChangeVolume(float volume)
    {
        current_volume = volume;
        final float result = (float) (1 - (Math.log(MAX_VOLUME - volume) / Math.log(MAX_VOLUME)));
        try{
            ring.setVolume(result, result);
        }
        catch(Exception e ) {
            System.out.println("Error : " + e);
        }

        SharedPref.SaveVolumePreferences( context,volume);
    }

}
