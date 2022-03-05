package com.example.myapplication.Lib;

import android.content.Context;
import android.widget.Toast;
import com.example.myapplication.MainMenu.LoadingActivity;

public class SharedPref {

    public static String loadIdClient(Context myActivity)  {
        android.content.SharedPreferences sharedPreferences= myActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);

        if(sharedPreferences != null)
        {
            return (sharedPreferences.getString("idClient","0"));

        } else
        {
            return("");
        }

    }

    public static void SaveIdClient(Context myActivity,String myIdClient)
    {
        android.content.SharedPreferences sharedPreferences = myActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("idClient",myIdClient);

        // Save.
        editor.apply();

        Toast.makeText(myActivity,"Setting saved!",Toast.LENGTH_LONG).show();
    }

}

