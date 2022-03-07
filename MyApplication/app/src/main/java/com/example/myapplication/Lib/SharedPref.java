package com.example.myapplication.Lib;

import android.content.Context;
import android.widget.Toast;

public class SharedPref
{

    /*
        Try lo load the idClient in the cache otherwise send nothing
     */
    public static String loadIdClient(Context myActivity)
    {
        android.content.SharedPreferences sharedPreferences= myActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);

        return ( sharedPreferences != null ? sharedPreferences.getString("idClient","0") : "" );
    }

    /*
        Save the id client in the cache
     */
    public static void SaveIdClient(Context myActivity,String myIdClient)
    {
        android.content.SharedPreferences sharedPreferences = myActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("idClient",myIdClient);
        editor.apply();

        Toast.makeText(myActivity,"Setting saved!",Toast.LENGTH_LONG).show();
    }

}

