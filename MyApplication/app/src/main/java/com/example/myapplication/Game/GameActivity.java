package com.example.myapplication.Game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.Dto.MapLine;
import com.example.myapplication.Lib.GameDesign;
import com.example.myapplication.Lib.Navigation;
import com.example.myapplication.Lib.TutoDesign;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GameActivity extends AppCompatActivity
{
    GridView gameBoard;
    Map myMap;
    int[] images = {R.drawable.perso,R.drawable.mur,R.drawable.sol,R.drawable.arrivee,R.drawable.boite,R.drawable.caisse_verte};
    private int[] matrix;
    private int[] matrixTemp;
    HashMap<Integer, MapLine> lesLinesMapsTemp;
    private int count;
    ImageButton left;
    ImageButton right;
    ImageButton up;
    ImageButton down;
    ImageButton restart;
    ImageButton quit;
    TextView view_text_level;
    private int currentPosition = 0;
    private int countNbBox;
    private int nbBoxPlaced;
    private int caseTemp = images[2];
    ArrayList<Integer> leftLimits = new ArrayList<>();
    ArrayList<Integer> rightLimits = new ArrayList<>();
    private boolean comingFromTest = false;
    int moveCount;
    TextView textMove;
    public PropertyChangeListener listener;
    private int currentStepTuto = 0;
    private boolean isTutuPlayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //---------------------- Tool selector -------------------------------- //

        view_text_level = findViewById(R.id.text_game_level);
        left = findViewById(R.id.button_game_left);
        right = findViewById(R.id.button_game_right);
        up = findViewById(R.id.button_game_up);
        down = findViewById(R.id.button_game_down);
        restart = findViewById(R.id.button_game_reload);
        quit = findViewById(R.id.button_game_goback);
        textMove = findViewById(R.id.textMoveCount);

        //-------------------------------------------------------------------- //

        //---------------------------Retrieve parameters----------------------- //

        myMap = (Map) getIntent().getSerializableExtra("Map");
        view_text_level.setText(myMap.getNom());
        Bundle args = new Bundle();
        comingFromTest = args.getBoolean("comingFromTest");
        textMove.setText("0");



        if (myMap.getIdMap() == -1){
            Map.HardCodedMap(this);
            try{
                if ( args.getBoolean("Tuto") )
                {
                    ShowMovement();
                }
            }catch (Exception e){}
        }
        else if (myMap.getIdMap() == -2){
            Map.FileMapLine(this);
        }
        else {
            MapDAO.GetMap(this, null, String.valueOf(myMap.getIdMap()));
        }


        //-------------------------------------------------------------------- //

        //---------------------- Set clicks actions -------------------------- //

        left.setOnClickListener(var ->  {
            if ( currentStepTuto == 0 ) {
                move( 1);
            }

        } );
        right.setOnClickListener(var -> {
            if ( currentStepTuto == 0 ) {
                move(-1);
            }
        } );
        up.setOnClickListener(var -> {
            if ( currentStepTuto == 0 ) {
                move(myMap.getNbColumns());
            }
        } );
        down.setOnClickListener(var -> {
            if ( currentStepTuto == 0 ) {
                move(-myMap.getNbColumns());
            }
        } );
        getMapLimits();
        restart.setOnClickListener(var -> {
            if ( currentStepTuto == 0 ) {
                RefreshGame();
            }
        });
        quit.setOnClickListener(var -> {
            if ( currentStepTuto == 0 ) {
                finish();
            }
        });

        //-------------------------------------------------------------------- //
    }

    /*
        Fill the gameBoard with the matrix
     */
    public void FillGameBoard()
    {
        this.runOnUiThread(() ->
        {
            gameBoard = findViewById(R.id.gameBoard);
            gameBoard.setColumnWidth( myMap.getNbColumns() * 4 );
            gameBoard.setNumColumns( myMap.getNbColumns() );

            GameDesign adapter = new GameDesign(this, images, matrix, myMap.getNbRows() * 30 );
            gameBoard.setAdapter(adapter);
        });
    }

    /*
        Called after the response of the API after retrieving all map lines
     */
    public void responseMapLine(HashMap<Integer, MapLine> lesLinesMaps )
    {
        matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];
        count = 0;
        countNbBox =0;

        List<MapLine> linesMapSorted = new ArrayList(lesLinesMaps.values());
        linesMapSorted.sort(Comparator.comparing(MapLine::getIndexRow));

        lesLinesMapsTemp = lesLinesMaps;

        linesMapSorted.forEach(MapLine ->
        {
            for (int i = 0; i < MapLine.getContent().length(); i++)
            {
                switch ( MapLine.getContent().charAt(i) )
                {
                    case 'P' :
                        matrix[ count ] = images[ 0 ];
                        currentPosition = count;
                        break;

                    case '#':
                        matrix[ count ] = images[ 1 ];
                        break;

                    case '.':
                        matrix[ count ] = images[ 2 ];
                        break;

                    case 'X':
                        matrix[ count ] = images[ 3 ];
                        break;

                    case 'C':
                        matrix[ count ] = images[ 4 ];
                        countNbBox++;
                        break;
                }
                count++;
            }
        });

        FillGameBoard();
    }

    /*
        Rules the physics movement of the player and move him in the gameBoard
     */
    public void move ( int movement )
    {
        int oldPosition = currentPosition;
        try
        {
            if ( matrix[ currentPosition - movement ] != images[ 1 ] )
            {
                if ( ( matrix[ currentPosition - movement ] == images[ 4 ] || matrix[ currentPosition - movement ] == images[ 5 ]  ) && ( matrix[ currentPosition -movement * 2 ] == images[ 2 ] || matrix[ currentPosition - movement * 2 ] == images[ 3 ] ))
                {
                    //check if a box is on the edge of the map
                    if ((!leftLimits.contains(currentPosition - movement) &&  - movement == -1) || (!rightLimits.contains(currentPosition - movement) && - movement == 1) ||  - movement != -1 &&  - movement != 1) {
                        //check if the player is on the edge of the map
                        if ((!leftLimits.contains(currentPosition) &&  - movement == -1) || (!rightLimits.contains(currentPosition) && - movement == 1) ||  - movement != -1 &&  - movement != 1) {

                            matrix[currentPosition] = caseTemp;
                            caseTemp = ( matrix[currentPosition - movement] == images[2] || matrix[currentPosition - movement] == images[3] ? caseTemp = matrix[currentPosition - movement] :  matrix[currentPosition - movement] == images[5] ? images[3] : images[2]) ;

                            if (matrix[currentPosition - movement] == images[5] )
                            {
                                matrix[currentPosition - movement ] = images[0];
                                if ( matrix[currentPosition - movement * 2] == images[3] )
                                {
                                    matrix[currentPosition - movement * 2] = images[5];
                                }
                                else
                                {
                                    nbBoxPlaced--;
                                    matrix[currentPosition - movement * 2] = images[4];
                                }

                                currentPosition -= movement;
                            }
                            else if ( matrix[currentPosition - movement] == images[4] && matrix[currentPosition - movement * 2] == images[3])
                            {
                                nbBoxPlaced++;
                                matrix[currentPosition - movement * 2] = images[5];
                                currentPosition -= movement;
                                matrix[currentPosition] = images[0];
                            }
                            else if ( matrix[currentPosition - movement] == images[4] )
                            {
                                matrix[currentPosition - movement * 2] = images[4];
                                currentPosition -= movement;
                                matrix[currentPosition] = images[0];
                            }
                            else
                            {
                                currentPosition -= movement;
                                matrix[currentPosition] = images[0];
                            }
                        }
                    }
                }
                else if ( matrix[ currentPosition - movement ] != images[ 4 ] &&  matrix[ currentPosition - movement ] != images[ 5 ])
                {

                    // check if the player is on the edge of the map
                    if ((!leftLimits.contains(currentPosition) &&  - movement == -1) || (!rightLimits.contains(currentPosition) && - movement == 1) ||  - movement != -1 &&  - movement != 1) {

                        matrix[currentPosition] = caseTemp;

                        caseTemp = ( matrix[currentPosition - movement] == images[2] || matrix[currentPosition - movement] == images[3] ? caseTemp = matrix[currentPosition - movement] :  matrix[currentPosition - movement] == images[5] ? images[3] : images[2]) ;

                        currentPosition -= movement;

                        matrix[currentPosition] = images[0];
                    }
                }
                if (oldPosition != currentPosition){ moveCount++; }
                textMove.setText(String.valueOf(moveCount));
                FillGameBoard();

                if ( nbBoxPlaced == countNbBox && comingFromTest && currentStepTuto == 0 )
                {
                    myMap.setIsTested(true);
                    MapDAO.UpdateIsTestMap( myMap );

                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE","true");
                    setResult(2,intent);
                    finish();//finishing activity
                }
                else if ( nbBoxPlaced == countNbBox && currentStepTuto == 0 )
                {
                    this.finish();
                }
            }
        }
        catch ( Exception ignored) {}

    }

    public void getMapLimits(){

        for(int i = 0; i < (myMap.getNbColumns() * myMap.getNbRows()); i++){
            double currentLine = Math.ceil(i/ myMap.getNbColumns());

            if (i % myMap.getNbColumns()  == 0 ){
                leftLimits.add(i);
            }
            else if ((i - currentLine) % (myMap.getNbColumns()-1)  == 0 ){
                rightLimits.add(i);
            }
        }
    }

    public void ShowMovement()
    {
        if ( currentStepTuto == 0 )
        {
            ArrayList<String> text = new ArrayList<>();
            text.add(" Bonjour et bienvenue dans Cave Escape ! Je m’appelle Noopy.Je suis ici pour vous expliquer les règles de mon jeu.");
            text.add( " L'objectif est simple : placer les boîtes sur les zones d'arriver comment ? voyons ça ensemble ! ");
            CallPopUp(text);
        }
        if ( currentStepTuto == 1 )
        {
            move( 1);
            Handler handler = new Handler();
            handler.postDelayed(() -> {

                move( 1);
                ArrayList<String> text = new ArrayList<>();
                text.add(" Remarquez que vous ne pouvez pas sortir de la map ! les murs sont des obstacles qui vous poserons souvent soucis réfléchissez bien ! ");
                text.add(" Allez déplaçons la caisse !");
                CallPopUp(text);

            }, 500);
        }
        else if ( currentStepTuto == 2 )
        {
            move( - myMap.getNbColumns());
            Handler handler = new Handler();
            handler.postDelayed(() -> {

                move( - myMap.getNbColumns());
                ArrayList<String> text = new ArrayList<>();
                text.add(" Voilà la partie est fini ! Vous pouvez vous amuser à travers les différents niveaux histoire ou communautaire ou créez en vous-même.");
                text.add(" Allez à votre tour bonne chance !");
                CallPopUp(text);

            }, 500);
        }
        else
        {
            RefreshGame();
        }
    }

    public void CallPopUp( ArrayList<String> text  )
    {
        TutoDesign popup = new TutoDesign(this,R.layout.popup_tuto,text);
        Dialog myDiag = new Dialog(this);
        myDiag.setCanceledOnTouchOutside(false);

        listener = event -> {
           RefreshTextPopUp(myDiag,popup,text);
        };


        popup.changes.addPropertyChangeListener(listener);

        Window window = myDiag.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        myDiag.setContentView( popup.getView( null ) );
        myDiag.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDiag.show();
    }

    public void RefreshGame()
    {
        moveCount = 0;
        nbBoxPlaced = 0;
        textMove.setText("0");
        currentStepTuto = 0;
        responseMapLine(lesLinesMapsTemp);
    }

    public void RefreshTextPopUp( Dialog myDiag, TutoDesign popup , ArrayList<String> text )
    {
        if (popup.listenerActive) {

            if ( popup.getCurrentText() == text.size() )
            {
                myDiag.dismiss();
                currentStepTuto++;
                ShowMovement();
            }
            else
            {
                myDiag.setContentView( popup.getView( null ) );
                myDiag.show();
                popup.listenerActive=false;
            }
        }
    }
}