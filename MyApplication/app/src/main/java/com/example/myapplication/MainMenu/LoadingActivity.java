package com.example.myapplication.MainMenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Lib.SharedPref;
import com.example.myapplication.Option.OptionActivity;
import com.example.myapplication.R;
import com.example.myapplication.Sandbox.SandboxMenuActivity;
import com.example.myapplication.LevelSelect.SelectActivity;
import com.example.myapplication.Lib.Navigation;

import java.net.URL;
import java.util.HashMap;

public class LoadingActivity extends AppCompatActivity
{
    Button button_play;
    Button button_sandbox;
    Button button_option;
    Button button_quit;
    HashMap params = new HashMap<>();
    public final static String CONNEXION_API = "http://192.168.1.96:8383/CaveEscapeServer/API.php";

    //idClient
    public static int idClient;
    private static int lastIdClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getIdClientFromPref();
        if(idClient < 1){
            System.out.println("idClient = " + idClient);
        }
        else{
            System.out.println("pas d'idClient");
            createIdClient();

        }
        System.out.println("verif idClient : " + idClient);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        button_play = findViewById(R.id.button_play);
        button_play.setOnClickListener(view -> Navigation.switchActivities(this, SelectActivity.class,params));

        button_sandbox = findViewById(R.id.button_sandbox);
        button_sandbox.setOnClickListener(view ->  Navigation.switchActivities(this, SandboxMenuActivity.class,params));

        button_option = findViewById(R.id.button_option);
        button_option.setOnClickListener(view -> Navigation.switchActivities(this, OptionActivity.class,params));

        button_quit = findViewById(R.id.button_quit);
        button_quit.setOnClickListener(view -> {

        });
    }

    private void createIdClient(){
        System.out.println("creation d'un idClient");
        MapDAO.getMaxiDClient(this);
        if(lastIdClient > 0){
            System.out.println("creation de l'idClient Reussie");
            idClient = lastIdClient + 1;
            System.out.println("Nouvel Id Client : " + idClient);
        }
    }

    private void getIdClientFromPref() {
        SharedPref.loadIdClient(this);
    }

    public static void setlastIdClient(Integer idCli){
        lastIdClient = idCli;
    }

}