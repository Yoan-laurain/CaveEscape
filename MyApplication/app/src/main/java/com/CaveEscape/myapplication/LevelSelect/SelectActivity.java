package com.CaveEscape.myapplication.LevelSelect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import com.bumptech.glide.Glide;
import com.CaveEscape.myapplication.Dao.MapDAO;
import com.CaveEscape.myapplication.Dto.Map;
import com.CaveEscape.myapplication.Game.GameActivity;
import com.CaveEscape.myapplication.Lib.LevelDesign;
import com.CaveEscape.myapplication.Lib.Navigation;
import com.CaveEscape.myapplication.Lib.SharedPref;
import com.CaveEscape.myapplication.MainMenu.LoadingActivity;
import com.CaveEscape.myapplication.R;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectActivity extends AppCompatActivity
{
    //-----------------------------------------

    Button button_return;
    ImageView background;
    ListView listLevel;

    //-----------------------------------------

    private final HashMap params = new HashMap<>();

    private ArrayList<String> mTitle = new ArrayList<>();
    private final ArrayList<Map> ListMap = new ArrayList<>();
    private final ArrayList<Integer> scoreList = new ArrayList<>();

    private boolean selectedMenu = false;
    private boolean Community = false;

    //-----------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //---------------------- Tool selector -------------------------------- //

        background = findViewById(R.id.View_BackGround_Select);
        button_return = findViewById(R.id.button_select_return);
        listLevel = findViewById(R.id.List_Level);

        //-------------------------------------------------------------------- //

        //---------------------- Set clicks actions -------------------------- //

        button_return.setOnClickListener(view -> ReturnAction());

        //-------------------------------------------------------------------- //

        Glide.with(this).load(R.drawable.selectback).into(background);

        DisplayMenuLevel();
    }

    /*
        Called after response of the API after retrieving all maps
        Read each values receive and create an adapter for each one
        Hydrate the list of level and set the action of on click on each items
     */
    public void ResponseMap(HashMap<Integer, Map> Maps)
    {
        this.runOnUiThread(() ->
        {
            LevelDesign adapter;

            mTitle.clear();
            ListMap.clear();
            scoreList.clear();

            if( !Community )
            {
                mTitle = new ArrayList<>();
                Map hardCoded = Map.HardCodedMapHeader();
                Maps.put(hardCoded.getIdMap(),hardCoded);
                Map fileMap = Map.FileMapHeader(this);
                Maps.put(fileMap.getIdMap(),fileMap);
            }
            if ( Maps.size() > 0 )
            {
                Maps.values().forEach(tab ->
                {
                    scoreList.add(SharedPref.LoadLevelScore(this, tab.getIdMap()));
                    mTitle.add( tab.getNom() );
                    ListMap.add( tab );
                });

                adapter = new LevelDesign(this,R.layout.row ,mTitle, scoreList);
                listLevel.setOnItemClickListener( (parent, view, position, id) -> OpenLevel( ListMap.get( position ) ) );
            }
            else
            {
                adapter = new LevelDesign(this,R.layout.row,new ArrayList<>(),new ArrayList<>());
            }
            listLevel.setAdapter(adapter);
        });
    }

    /*
        Open the page game with the id of the level in parameter
     */
    public void OpenLevel( Map mapSelected )
    {
        params.put("Map", mapSelected);
        Navigation.switchActivities(this, GameActivity.class,params);
    }

    /*
        Display the the menu to choose between History or community
     */
    public void DisplayMenuLevel()
    {
        ListView listLevel = findViewById(R.id.List_Level);

        ArrayList<String> bTitle = new ArrayList<>();
        LevelDesign adapter;

        bTitle.add("\n \n \n History");
        bTitle.add("\n \n \n Community");

        adapter = new LevelDesign(this,R.layout.row_title ,bTitle , new ArrayList<>());
        listLevel.setOnItemClickListener( (parent, view, position, id) -> FillListLevel(bTitle.get(position)));
        listLevel.setAdapter(adapter);
    }

    /*
        Fill the list level depending on which section the player has chosen
     */
    public void FillListLevel( String title )
    {
        selectedMenu = true;
        switch( title )
        {
            case "\n \n \n Community" :
                Community = true;
                MapDAO.GetCommunityMap(this,null);
                break;

            case "\n \n \n History" :
                Community = false;
                MapDAO.GetHistoryMap(this,null);
                break;

            default:
                break;
        }
    }

    /*
         Return to the previous page depending if the player already has selected game mode or hes actually choosing
     */
    public void ReturnAction()
    {
        if ( selectedMenu )
        {
            selectedMenu = false;
            DisplayMenuLevel();
        }
        else
        {
            Navigation.switchActivities(this, LoadingActivity.class, params);
        }
    }

}
