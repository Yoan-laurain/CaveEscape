package com.example.myapplication.Lib;

import android.content.Context;
import android.content.Intent;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class Navigation
{

    public static void switchActivities(Context depart, Class arrive, HashMap<Key,Object> params)
    {
        Intent NewActivities = new Intent( depart, arrive );

        for (Map.Entry mapEntry : params.entrySet() )
        {
            NewActivities.putExtra( mapEntry.getKey().toString(),  mapEntry.getValue().toString() );
        }

        depart.startActivity(NewActivities);
    }

}
