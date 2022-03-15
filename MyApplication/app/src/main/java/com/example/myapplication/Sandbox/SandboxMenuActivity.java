package com.example.myapplication.Sandbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.Lib.LevelDesign;
import com.example.myapplication.Lib.SandBoxLevelDesigner;
import com.example.myapplication.MainMenu.LoadingActivity;
import com.example.myapplication.R;
import com.example.myapplication.Lib.Navigation;

import java.util.ArrayList;
import java.util.HashMap;

public class SandboxMenuActivity extends AppCompatActivity
{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          Variables                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // ---------------------------------- Retrieving Visuals elements ----------------------------//
    Button button_return;
    ImageView background;
    Button button_create;

    // ---------------------------------------- Variables ----------------------------------------//
    HashMap params = new HashMap<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<Boolean> mIsTested = new ArrayList<>();
    private ArrayList<Map> ListMap = new ArrayList<>();

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                            Code                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // ------------------------ this method is called when the page is open ----------------------//
    // ------------------------------ it create the list of maps. ------------------------------- //
    @Override
    protected void onResume(){
        super.onResume();
        if ( LoadingActivity.idClient.equals("fNzlhc9eBfZm") || LoadingActivity.idClient.equals("BKxkND1Bb80N") || LoadingActivity.idClient.equals("fNzlhc9eBfZm"))
        {
            System.out.println("MARKER");
            MapDAO.GetAllMap(this);
        }
        else
        {
            MapDAO.getMapByClient(null,this);
        }

    }

    // ---------------------------------- Loading Visuals elements ----------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox_menu);

        background = findViewById(R.id.View_BackGround_Sandbox_Menu);
        Glide.with(this).load(R.drawable.sandboxback).into(background);

        button_return = findViewById(R.id.button_sandboxmenu_return);
        button_return.setOnClickListener(view -> Navigation.switchActivities(this, LoadingActivity.class,params));

        button_create = findViewById(R.id.button_edit_sandbox);
        button_create.setOnClickListener(view -> openCreateMap());

    }


    // ------------------------- Called after response of the API --------------------------------//
    // -------------- Read each values receive and create an adapter for each one ----------------//
    // ----------- Hydrate the list of level and set the action of on click on each items --------//

    public void responseMap(HashMap<Integer, Map> lesMaps)
    {
        this.runOnUiThread(() ->
        {
            if ( lesMaps.size() > 0 )
            {
                mTitle = new ArrayList<>();
                mIsTested = new ArrayList<>();
                ListMap = new ArrayList<>();
                lesMaps.values().forEach(tab -> {
                    mTitle.add(tab.getNom());
                    mIsTested.add(tab.getIsTested());
                    ListMap.add(tab);
                });
                ListView listLevel = findViewById(R.id.List_Level_SandBox);
                SandBoxLevelDesigner adapter = new SandBoxLevelDesigner(this,R.layout.sandbox_level ,mTitle,mIsTested);
                listLevel.setAdapter(adapter);

                listLevel.setOnItemClickListener( (parent, view, position, id) -> openLevel( ListMap.get( position ) ) );
            }
            else
            {
                ListView listLevel = findViewById(R.id.List_Level_SandBox);
                SandBoxLevelDesigner adapter = new SandBoxLevelDesigner(this,R.layout.row,new ArrayList<>(),new ArrayList<>());
                listLevel.setAdapter(adapter);
            }
        });
    }


    // ------------ Open the page game with the id of the level in parameter ---------------------//

    public void openLevel(Map mapSelected)
    {
        params.put("Map", mapSelected);
        Navigation.switchActivities(this, SandboxActivity.class,params);
    }

    public void openCreateMap()
    {
        params.put("Map",null);
        Navigation.switchActivities(this, SandboxActivity.class,params);
    }
}
