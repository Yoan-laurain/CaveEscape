package com.example.myapplication.Lib;

import android.content.Context;
import android.content.Intent;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class Navigation
{
    private static Intent NewActivities;

    public static void switchActivities(Context depart, Class arrive, HashMap<Key,Object> params)
    {
        Intent NewActivities = new Intent( depart, arrive );

        for (Map.Entry mapEntry : params.entrySet() )
        {

            if ( mapEntry.getValue() instanceof Integer )
            {
                NewActivities.putExtra( mapEntry.getKey().toString(), (int) mapEntry.getValue() );
            }
            else if (  mapEntry.getValue() instanceof Boolean )
            {
                NewActivities.putExtra( mapEntry.getKey().toString(), (boolean) mapEntry.getValue() );
            }
            else if ( mapEntry.getValue() instanceof String )
            {
                NewActivities.putExtra( mapEntry.getKey().toString(),(String) mapEntry.getValue()  );
            }
        }
        depart.startActivity(NewActivities);
    }
}
    



