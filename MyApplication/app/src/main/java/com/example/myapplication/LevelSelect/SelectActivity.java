package com.example.myapplication.LevelSelect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import android.widget.ListView;
import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.Game.GameActivity;
import com.example.myapplication.Lib.LevelDesign;
import com.example.myapplication.MainMenu.LoadingActivity;
import com.example.myapplication.R;
import com.example.myapplication.Lib.Navigation;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectActivity extends AppCompatActivity
{
    Button button_return;
    ImageView background;
    HashMap params = new HashMap<>();
    private ArrayList<String> mTitle = new ArrayList();
    private ArrayList<Map> ListMap = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        background = findViewById(R.id.View_BackGround_Select);
        Glide.with(this).load(R.drawable.selectback).into(background);

        button_return = findViewById(R.id.button_select_return);
        button_return.setOnClickListener(view -> Navigation.switchActivities(this, LoadingActivity.class,params));

        MapDAO.getAllMap(this);
    }

    /*
        Called after response of the API
        Read each values receive and create an adapter for each one
        Hydrate the list of level and set the action of on click on each items
     */
    public void responseMap(HashMap<Integer, Map> lesMaps)
    {
        this.runOnUiThread(() ->
        {
            if ( lesMaps.size() > 0 )
            {
                mTitle = new ArrayList<>();


                lesMaps.values().forEach(tab -> {
                    mTitle.add(tab.getNom());
                    ListMap.add(tab);
                });

                ListView listLevel = findViewById(R.id.List_Level);
                LevelDesign adapter = new LevelDesign(this,R.layout.row ,mTitle);
                listLevel.setAdapter(adapter);

                listLevel.setOnItemClickListener( (parent, view, position, id) -> openLevel( ListMap.get( position ) ) );

            }
            else
            {
                ListView listLevel = findViewById(R.id.List_Level);
                LevelDesign adapter = new LevelDesign(this,R.layout.row,new ArrayList());
                listLevel.setAdapter(adapter);
            }
        });
    }

    /*
        Open the page game with the id of the level in parameter
     */
    public void openLevel(Map mapSelected)
    {
        params.put("Map", mapSelected);
        System.out.println("Objatino : " + mapSelected);
        Navigation.switchActivities(this, GameActivity.class,params);
    }

}
