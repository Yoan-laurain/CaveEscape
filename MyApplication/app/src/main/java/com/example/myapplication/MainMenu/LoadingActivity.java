package com.example.myapplication.MainMenu;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Lib.AudioPlayer;
import com.example.myapplication.Lib.Security;
import com.example.myapplication.Lib.SharedPref;
import com.example.myapplication.Option.OptionActivity;
import com.example.myapplication.R;
import com.example.myapplication.Sandbox.SandboxMenuActivity;
import com.example.myapplication.LevelSelect.SelectActivity;
import com.example.myapplication.Lib.Navigation;

import java.util.HashMap;

public class LoadingActivity extends AppCompatActivity
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          Variables                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // ---------------------------------- Retrieving Visuals elements ----------------------------//

    Button button_play;
    Button button_sandbox;
    Button button_option;
    Button button_quit;
    ImageView background;

    // ---------------------------------- Variables ----------------------------------------------//
    HashMap params = new HashMap<>();
    public final static String CONNEXION_API = "http://51.254.96.53:8383/api.php";
    public final static String API_KEY = "43d24893a0ca4dfdacbbc6f0b3067804";
    //public final static String CONNEXION_API = "http://192.168.1.96:8383/CaveEscapeServer/API.php";
    //public final static String CONNEXION_API = "http://192.168.0.14/CaveEscapeServer/API.php";

    public static String idClient;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          Code                                              //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        if (!AudioPlayer.isAnEasterEgg){
            AudioPlayer.Play(this, R.raw.mainmusic);
        }
        getIdClientFromPref();
        System.out.println("idClient récupéré : " + idClient);
        if(idClient.equals("0")){
            createIdClient();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // ----------------------------- Loading Visual Elements ---------------------------------//

        background = findViewById(R.id.View_BackGround_Loading);
        Glide.with(this).load(R.drawable.jgif).into(background);

        button_play = findViewById(R.id.button_play);
        button_play.setOnClickListener(view -> Navigation.switchActivities(this, SelectActivity.class,params));

        button_sandbox = findViewById(R.id.button_sandbox);
        button_sandbox.setOnClickListener(view ->  Navigation.switchActivities(this, SandboxMenuActivity.class,params));

        button_option = findViewById(R.id.button_option);
        button_option.setOnClickListener(view -> Navigation.switchActivities(this, OptionActivity.class,params));

        button_quit = findViewById(R.id.button_quit);
        button_quit.setOnClickListener(view -> {

            AudioPlayer.stop();
            finishAffinity();
        });
    }

    // --------------------------------- Create a New Id Client  ---------------------------------//
    private void createIdClient(){
        idClient = Security.RandomToken(12);
        SharedPref.SaveIdClient(this, idClient);
    }

    // -------------------------------- Get The idClient from the cache --------------------------//
    private void getIdClientFromPref() {
        idClient = SharedPref.loadIdClient(this);
    }

}