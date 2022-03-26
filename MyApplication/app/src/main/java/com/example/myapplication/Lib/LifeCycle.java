package com.example.myapplication.Lib;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;

public class LifeCycle extends Application implements LifecycleObserver {

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        AudioPlayer.getRing().pause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded()
    {
        try
        {
            AudioPlayer.Resume();
        }
        catch(Exception e){
            System.out.println(" Error : " + e);
        }

    }
}