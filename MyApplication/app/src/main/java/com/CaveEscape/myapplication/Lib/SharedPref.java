package com.CaveEscape.myapplication.Lib;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref
{

    /*
        Try lo load the idClient in the cache otherwise send nothing
     */
    public static String LoadIdClient(Context myActivity)
    {
        SharedPreferences sharedPreferences= myActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);

        return ( sharedPreferences != null ? sharedPreferences.getString("idClient","0") : "" );
    }

    /*
        Save the id client in the cache
     */
    public static void SaveIdClient(Context myActivity,String myIdClient)
    {
        SharedPreferences sharedPreferences = myActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("idClient",myIdClient);
        editor.apply();
    }

    //----------------------------------------------------------------------------------------------------------

    /*
        Save volume preferences
    */
    public static void SaveVolumePreferences(Context myActivity,float volume)
    {
        SharedPreferences sharedPreferences = myActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Volume",String.valueOf( volume ) );
        editor.apply();
    }

    /*
    Try lo load the volume in the cache otherwise send nothing
 */
    public static float LoadVolumePreferences(Context myActivity)
    {
        SharedPreferences sharedPreferences= myActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return ( sharedPreferences != null ? Float.parseFloat( sharedPreferences.getString("Volume","80") ) : 100 );
    }

    //----------------------------------------------------------------------------------------------------------

    /*
    save Score from a level
     */
    public static void SaveLevelScore(Context myActivity, int idMap, int score){

        SharedPreferences sharedPreferences = myActivity.getSharedPreferences("Scores",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(String.valueOf(idMap), String.valueOf(score));
        editor.apply();
    }

    public static int LoadLevelScore(Context myActivity, int idMap){
        SharedPreferences sharedPreferences = myActivity.getSharedPreferences("Scores",Context.MODE_PRIVATE);
        return ( sharedPreferences != null ? Integer.parseInt(sharedPreferences.getString(String.valueOf(idMap),"0")) : 0 );
    }
}

