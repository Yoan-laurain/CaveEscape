package com.example.myapplication.Game;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.Dto.MapLine;
import com.example.myapplication.Lib.EndGame;
import com.example.myapplication.Lib.GameDesign;
import com.example.myapplication.Lib.TutoDesign;
import com.example.myapplication.R;


import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.R)
public class GameActivity extends AppCompatActivity
{
    GridView gameBoard;
    Map myMap;
    int[] images = {R.drawable.left_player_blue,R.drawable.mur,R.drawable.blue_grass,R.drawable.opened_cage_blue,R.drawable.free_monster_blue,R.drawable.caged_monster_blue};
    private int[] matrix;
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
    private boolean tuto = false;

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

        try{
            comingFromTest = (boolean) getIntent().getSerializableExtra("comingFromTest");
        }
        catch(Exception e){}

        textMove.setText("0");

        if (myMap.getIdMap() == -1){
            Map.HardCodedMap(this);
            try{

                Bundle extras = getIntent().getExtras();
                String isTuto = extras.getString("Tuto");

                if ( isTuto.equals("true") )
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

            GameDesign adapter = new GameDesign(this, images, matrix, gameBoard.getHeight() / myMap.getNbRows());
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
                        if ((!leftLimits.contains(currentPosition) &&  - movement == -1) || (!rightLimits.contains(currentPosition) && - movement == 1) ||  - movement != -1 &&  - movement != 1)
                        {
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
                    MapDAO.UpdateIsTestMap( this,null,myMap );

                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE","true");
                    setResult(2,intent);
                    finish();
                }
                else if ( nbBoxPlaced == countNbBox && currentStepTuto == 0 && !tuto)
                {
                    System.out.println("NOOPY");
                    callPopUpEndGame();
                }
                else if ( nbBoxPlaced == countNbBox && tuto  )
                {
                    finish();
                }
            }
        }
        catch ( Exception ignored) {}

    }

    public void getMapLimits(){

        leftLimits = new ArrayList<>();
        rightLimits = new ArrayList<>();

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
            text.add(" Hello and welcome young hero! My name is Noopy. Our kingdom is under attack, we need your help.");
            text.add( "There are aztaroths in the kingdom, they must be locked up and quickly! They will destroy everything!!");
            text.add( " I show you how to do it and then it's your turn ok? ");
            CallPopUp(text);
        }
        if ( currentStepTuto == 1 )
        {
            move( 1);
            Handler handler = new Handler();
            handler.postDelayed(() -> {

                move( 1);
                ArrayList<String> text = new ArrayList<>();
                text.add(" You need to be careful there are walls! ");
                text.add(" Come on, let's bring this aztaroth back to its cage and quickly !");
                CallPopUp(text);

            }, 500);
        }
        else if ( currentStepTuto == 2 )
        {
            move( - myMap.getNbColumns());
            Handler handler = new Handler();
            tuto = false;
            handler.postDelayed(() -> {

                move( - myMap.getNbColumns());
                ArrayList<String> text = new ArrayList<>();
                text.add(" Well the job is done! Thank you very much. The kingdom still needs your help you know. ");
                text.add(" You can create levels if you want but it's not nice there are already a lot of monsters...");
                text.add(" Now it's you're turn good luck ! ");
                CallPopUp(text);

            }, 500);
        }
        else
        {
            RefreshGame();
            tuto = true;
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

    public void callPopUpEndGame()
    {
        EndGame popup = new EndGame(this,R.layout.popup_end_game);
        Dialog myDiag = new Dialog(this);
        myDiag.setCanceledOnTouchOutside(false);

        Window window = myDiag.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        myDiag.setContentView( popup.getView( null ) );

        popup.getReplayGame().setOnClickListener(var -> {
            RefreshGame();
            myDiag.dismiss();
        });

        popup.getNextLevel().setOnClickListener(var -> {
            MapDAO.GetNextMap(this,myMap.getIdMap() );
            myDiag.dismiss();
        });

        popup.getReturn_back().setOnClickListener(var -> finish());

        myDiag.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDiag.show();
    }

    public void RefreshGame()
    {
        moveCount = 0;
        nbBoxPlaced = 0;
        textMove.setText("0");
        view_text_level.setText(myMap.getNom());
        currentStepTuto = 0;
        caseTemp = images[2];
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

    public void ResponseNextLevel(Map newMap)
    {
        this.runOnUiThread(() ->
        {
            myMap = newMap;

            MapDAO.GetMap(this, null, String.valueOf(myMap.getIdMap()));
            moveCount = 0;
            nbBoxPlaced = 0;
            textMove.setText("0");
            view_text_level.setText(myMap.getNom());
            currentStepTuto = 0;
            caseTemp = images[2];
            getMapLimits();
        });
    }
}