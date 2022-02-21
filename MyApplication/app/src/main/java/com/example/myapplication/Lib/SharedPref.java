package com.example.myapplication.Lib;

import android.content.Context;
import android.widget.Toast;
import com.example.myapplication.MainMenu.LoadingActivity;

public class SharedPref {

    public static void loadIdClient(Context myActivity)  {
        android.content.SharedPreferences sharedPreferences= myActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);

        if(sharedPreferences != null)
        {
            LoadingActivity.idClient = Integer.valueOf(sharedPreferences.getString("idClient","0"));

        } else
        {

        }
    }

    public static void SaveIdClient(Context myActivity,Integer myIdClient)
    {
        android.content.SharedPreferences sharedPreferences = myActivity.getSharedPreferences("Param", Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("idClient",String.valueOf(myIdClient));

        // Save.
        editor.apply();

        Toast.makeText(myActivity,"Setting saved!",Toast.LENGTH_LONG).show();
    }

}

