package com.example.myapplication.Sandbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import com.bumptech.glide.Glide;
import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.Lib.SandBoxLevelDesigner;
import com.example.myapplication.MainMenu.LoadingActivity;
import com.example.myapplication.R;
import com.example.myapplication.Lib.Navigation;

import java.util.ArrayList;
import java.util.HashMap;

public class SandboxMenuActivity extends AppCompatActivity
{

    // ---------------------------------- Retrieving Visuals elements ----------------------------//

    Button button_return;
    ImageView background;
    Button button_create;

    //--------------------------------------------------------------------------------------------//

    // ---------------------------------------- Variables ----------------------------------------//

    HashMap params = new HashMap<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<Boolean> mIsTested = new ArrayList<>();
    private ArrayList<Map> ListMap = new ArrayList<>();

    //--------------------------------------------------------------------------------------------//

    /*
        This method is called when the page is open
        it create the list of maps.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        if ( LoadingActivity.idClient.equals("fNzlhc9eBfZm") || LoadingActivity.idClient.equals("BKxkND1Bb80N") || LoadingActivity.idClient.equals("fNzlhc9eBfZm"))
        {
            MapDAO.GetAllMap(this);
        }
        else
        {
            MapDAO.getMapByClient(null,this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox_menu);

        //---------------------- Tool selector -------------------------------- //

        background = findViewById(R.id.View_BackGround_Sandbox_Menu);
        button_return = findViewById(R.id.button_sandboxmenu_return);
        button_create = findViewById(R.id.button_edit_sandbox);

        //-------------------------------------------------------------------- //

        //---------------------- Set clicks actions -------------------------- //

        button_return.setOnClickListener(view -> Navigation.switchActivities(this, LoadingActivity.class,params));
        button_create.setOnClickListener(view -> OpenCreateMap());

        //-------------------------------------------------------------------- //

        Glide.with(this).load(R.drawable.sandboxback).into(background);
    }

    /*
        Called after response of the API
        Read each values receive and create an adapter for each on
        Hydrate the list of level and set the action of on click on each items
    */

    public void ResponseMap(HashMap<Integer, Map> lesMaps)
    {
        this.runOnUiThread(() ->
        {
            if ( lesMaps.size() > 0 )
            {
                mTitle = new ArrayList<>();
                mIsTested = new ArrayList<>();
                ListMap = new ArrayList<>();

                lesMaps.values().forEach(tab ->
                {
                    mTitle.add(tab.getNom());
                    mIsTested.add(tab.getIsTested());
                    ListMap.add(tab);
                });
                ListView listLevel = findViewById(R.id.List_Level_SandBox);
                SandBoxLevelDesigner adapter = new SandBoxLevelDesigner(this,R.layout.sandbox_level ,mTitle,mIsTested);
                listLevel.setAdapter(adapter);

                listLevel.setOnItemClickListener( (parent, view, position, id) -> OpenLevel( ListMap.get( position ) ) );
            }
            else
            {
                ListView listLevel = findViewById(R.id.List_Level_SandBox);
                SandBoxLevelDesigner adapter = new SandBoxLevelDesigner(this,R.layout.row,new ArrayList<>(),new ArrayList<>());
                listLevel.setAdapter(adapter);
            }
        });
    }

    /*
        Open the page game with the id of the level in parameter
     */

    public void OpenLevel(Map mapSelected)
    {
        params.put("Map", mapSelected);
        Navigation.switchActivities(this, SandboxActivity.class,params);
    }

    /*
        Open the the creation map
     */
    public void OpenCreateMap()
    {
        params.put("Map",null);
        Navigation.switchActivities(this, SandboxActivity.class,params);
    }
}
