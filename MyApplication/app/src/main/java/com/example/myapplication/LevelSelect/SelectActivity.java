package com.example.myapplication.LevelSelect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.Game.GameActivity;
import com.example.myapplication.Lib.LevelDesign;
import com.example.myapplication.Lib.Navigation;
import com.example.myapplication.MainMenu.LoadingActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SelectActivity extends AppCompatActivity
{
    Button button_return;
    ImageView background;
    HashMap params = new HashMap<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private final ArrayList<Map> ListMap = new ArrayList<>();
    boolean selectedMenu = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        //---------------------- Tool selector -------------------------------- //

        background = findViewById(R.id.View_BackGround_Select);
        button_return = findViewById(R.id.button_select_return);
        ListView listLevel = findViewById(R.id.List_Level);


        //-------------------------------------------------------------------- //

        //---------------------- Set clicks actions -------------------------- //

        button_return.setOnClickListener(view -> returnAction());

        //-------------------------------------------------------------------- //

        Glide.with(this).load(R.drawable.selectback).into(background);

        printChoices();

    }

    /*
        Called after response of the API after retrieving all maps
        Read each values receive and create an adapter for each one
        Hydrate the list of level and set the action of on click on each items
     */
    public void responseMap(HashMap<Integer, Map> Maps)
    {
        this.runOnUiThread(() ->
        {
            ListView listLevel = findViewById(R.id.List_Level);
            LevelDesign adapter;

            
            mTitle.clear();
            ListMap.clear();

            if ( Maps.size() > 0 )
            {

                mTitle = new ArrayList<>();
                Map hardCoded = Map.HardCodedMapHeader();
                Maps.put(hardCoded.getIdMap(),hardCoded);
                Map fileMap = Map.FileMapHeader(this);
                Maps.put(fileMap.getIdMap(),fileMap);

                Maps.values().forEach(tab -> {
                    mTitle.add( tab.getNom() );
                    ListMap.add( tab );
                });
                //Collections.sort(mTitle);
                adapter = new LevelDesign(this,R.layout.row ,mTitle);
                listLevel.setOnItemClickListener( (parent, view, position, id) -> openLevel( ListMap.get( position ) ) );
            }
            else
            {
                adapter = new LevelDesign(this,R.layout.row,new ArrayList<>());
            }
            listLevel.setAdapter(adapter);
        });
    }

    /*
        Open the page game with the id of the level in parameter
     */
    public void openLevel(Map mapSelected)
    {
        params.put("Map", mapSelected);
        Navigation.switchActivities(this, GameActivity.class,params);
    }

    public void printChoices(){

        ListView listLevel = findViewById(R.id.List_Level);

        ArrayList<String> bTitle = new ArrayList<>();
        LevelDesign adapter;

        bTitle.add("\n \n \n History");
        bTitle.add("\n \n \n Community");

        adapter = new LevelDesign(this,R.layout.row_title ,bTitle);
        listLevel.setOnItemClickListener( (parent, view, position, id) -> callRightMethod(bTitle.get(position)));
        listLevel.setAdapter(adapter);
    }

    public void callRightMethod(String titre ){

        selectedMenu = true;
        switch(titre){
            case "\n \n \n Community" :
                MapDAO.GetCommunityMap(this,null);
                break;

            case "\n \n \n History" :
                MapDAO.GetHistoryMap(this,null);
                break;

            default:
                break;
        }
    }

    public void returnAction(){
        if (selectedMenu) {
            selectedMenu = false;
            printChoices();
        } else {
            Navigation.switchActivities(this, LoadingActivity.class, params);
        }
    }

}
