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

            switch (  mapEntry.getValue().getClass().getName()  )
            {
                case "java.lang.Integer" :
                    NewActivities.putExtra( mapEntry.getKey().toString(), (int) mapEntry.getValue() );

                case "java.lang.String":
                    NewActivities.putExtra( mapEntry.getKey().toString(),  mapEntry.getValue().toString()  );

                case "java.lang.Boolean":
                    NewActivities.putExtra( mapEntry.getKey().toString(), (boolean) mapEntry.getValue() );

                case "java.lang.Character":
                    NewActivities.putExtra( mapEntry.getKey().toString(),  (char) mapEntry.getValue()  );

                case "java.lang.Byte":
                    NewActivities.putExtra( mapEntry.getKey().toString(),  (byte) mapEntry.getValue()  );

                case "java.lang.Short":
                    NewActivities.putExtra( mapEntry.getKey().toString(),  (short) mapEntry.getValue()  );

                case "java.lang.Long":
                    NewActivities.putExtra( mapEntry.getKey().toString(),  (long) mapEntry.getValue()  );

                case "java.lang.Float":
                    NewActivities.putExtra( mapEntry.getKey().toString(),  (float) mapEntry.getValue()  );

                case "java.lang.Double":
                    NewActivities.putExtra( mapEntry.getKey().toString(),  (double) mapEntry.getValue()  );
            }



        }

        depart.startActivity(NewActivities);
    }

}
