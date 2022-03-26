package com.example.myapplication.Game;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.Dto.MapLine;
import com.example.myapplication.LevelSelect.SelectActivity;
import com.example.myapplication.Lib.EndGame;
import com.example.myapplication.Lib.GameDesign;
import com.example.myapplication.Lib.Navigation;
import com.example.myapplication.Lib.SharedPref;
import com.example.myapplication.Lib.TutoDesign;
import com.example.myapplication.R;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class GameActivity extends AppCompatActivity
{
    //-------------------------------------

    ImageButton left;
    ImageButton right;
    ImageButton up;
    ImageButton down;
    ImageButton restart;
    ImageButton quit;
    ImageButton rollBack;
    TextView view_text_level;
    TextView textMove;
    GridView gameBoard;
    ImageView scoreStars;

    //-------------------------------------

    private final int[] images = {R.drawable.left_player_blue,R.drawable.mur,R.drawable.blue_grass,R.drawable.opened_cage_blue,R.drawable.free_monster_blue,R.drawable.caged_monster_blue,R.drawable.player_on_cage};

    private final int[] wallsTab = {R.drawable.bottom_left_angle_wall_blue,R.drawable.bottom_right_angle_wall_blue,
            R.drawable.bottom_vertical_wall_blue,R.drawable.middle_vertical_wall_blue,R.drawable.middle_straight_wall_blue,
            R.drawable.left_straight_wall_blue,R.drawable.right_straight_wall_blue,R.drawable.top_left_angle_wall_blue,
            R.drawable.top_right_angle_wall_blue,R.drawable.top_vertical_wall_blue,R.drawable.top_t_wall50,
            R.drawable.bottom_t_wall50,R.drawable.left_t_wall,R.drawable.right_t_wall,R.drawable.cross_wall};

    HashMap<Character,Integer> wallRelation = new HashMap<Character,Integer>(){{
        put('A',    wallsTab[0]);
        put('B',    wallsTab[1]);
        put('V',    wallsTab[2]);
        put('D',    wallsTab[3]);
        put('E',    wallsTab[4]);
        put('F',    wallsTab[5]);
        put('G',    wallsTab[6]);
        put('H',    wallsTab[7]);
        put('I',    wallsTab[8]);
        put('J',    wallsTab[9]);
        put('T',    wallsTab[10]);
        put('U',    wallsTab[11]);
        put('>',    wallsTab[12]);
        put('<',    wallsTab[13]);
        put('+',    wallsTab[14]);

    }};


    private int count;
    private int currentPosition = 0;
    private int countNbBox;
    private int nbBoxPlaced;
    private int caseTemp = images[2];
    private int previousCaseTemp = images[2];
    private int moveCount;
    private int currentStepTuto = 0;
    private int[] matrix;
    private int[] previousMatrix;
    private int loadScore = 0;

    private boolean comingFromTest = false;
    private boolean tuto = false;

    private ArrayList<Integer> leftLimits = new ArrayList<>();
    private ArrayList<Integer> rightLimits = new ArrayList<>();

    private Map myMap;
    private HashMap<Integer, MapLine> linesMapsTemp;
    public PropertyChangeListener listener;

    private LinkedHashMap<int[], Integer> listHistoryMap = new LinkedHashMap<>();

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
        gameBoard = findViewById(R.id.gameBoard);
        rollBack = findViewById(R.id.button_game_undo);
        scoreStars = findViewById(R.id.score_stars);


        //-------------------------------------------------------------------- //

        //---------------------------Retrieve parameters----------------------- //

        myMap = (Map) getIntent().getSerializableExtra("Map");


        try
        {
            comingFromTest = (boolean) getIntent().getSerializableExtra("comingFromTest");
        }catch(Exception ignored){}

        //-------------------------------------------------------------------- //

        //---------------------------Change interface----------------------- //

        view_text_level.setText( myMap.getNom() );
        textMove.setText("0");

        //-------------------------------------------------------------------- //

        //---------------------------LOAD MAP----------------------- //

        if ( myMap.getIdMap() == -1 )
        {
            Map.HardCodedMap(this);
            try
            {
                String isTuto = (String) getIntent().getSerializableExtra("Tuto");

                if ( isTuto.equals("true") )
                {
                    TutorialGuide();
                }
            }catch (Exception ignored){}
        }
        else if (myMap.getIdMap() == -2)
        {
            Map.FileMapLine(this);
        }
        else
        {
            MapDAO.GetMap(this, null, String.valueOf(myMap.getIdMap()));
        }

        //-------------------------------------------------------------------- //

        //---------------------- Set clicks actions -------------------------- //

        left.setOnClickListener(var ->  {
            if ( currentStepTuto == 0 ) {
                Move( 1);
            }

        } );
        right.setOnClickListener(var -> {
            if ( currentStepTuto == 0 ) {
                Move(-1);
            }
        } );
        up.setOnClickListener(var -> {
            if ( currentStepTuto == 0 ) {
                Move(myMap.getNbColumns());
            }
        } );
        down.setOnClickListener(var -> {
            if ( currentStepTuto == 0 ) {
                Move(-myMap.getNbColumns());
            }
        } );
        GetMapLimits();
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
        rollBack.setOnClickListener(var -> RollBackAction());

        //-------------------------------------------------------------------- //
    }

    /*
        Fill the gameBoard with the matrix
     */
    public void FillGameBoard()
    {

        this.runOnUiThread(() ->
        {
            gameBoard.setColumnWidth( myMap.getNbColumns() * 4 );
            gameBoard.setNumColumns( myMap.getNbColumns() );

            ViewGroup.LayoutParams params = gameBoard.getLayoutParams();


            int gameBoardHeight = gameBoard.getHeight() / myMap.getNbRows();
            //int gameBoardWidth = gameBoard.getWidth() / myMap.getNbColumns();

            if ( myMap.getIdMap() == -1 || myMap.getIdMap() == -2 )
            {
                if(gameBoardHeight > 300){
                    gameBoardHeight = 200;
                }
                else{
                    gameBoardHeight = 140;
                }
            }

            // check the height of a line is good to display
            if(gameBoardHeight > 300){
                gameBoardHeight = 300;
                params.height = myMap.getNbRows() * gameBoardHeight;
            }

            GameDesign adapter = new GameDesign(this, matrix, gameBoardHeight);
            gameBoard.setAdapter(adapter);
        });
    }

    /*
        Called after the response of the API after retrieving all map lines
     */
    public void ResponseMapLine( HashMap<Integer, MapLine> linesMaps )
    {
        loadScore = SharedPref.LoadLevelScore(this,myMap.getIdMap());
        this.runOnUiThread(() ->
                {
                    switch (loadScore) {
                        case 1:
                            scoreStars.setImageResource(R.drawable.one_star);
                            break;
                        case 2:
                            scoreStars.setImageResource(R.drawable.two_stars);
                            break;
                        case 3:
                            scoreStars.setImageResource(R.drawable.three_stars);
                            break;
                        default:
                            scoreStars.setImageResource(R.drawable.zero_stars);
                            break;
                    }
                });

        matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];
        listHistoryMap = new LinkedHashMap<>();

        count = 0;
        countNbBox =0;

        List<MapLine> linesMapSorted = new ArrayList<>(linesMaps.values());
        linesMapSorted.sort(Comparator.comparing(MapLine::getIndexRow));

        linesMapsTemp = linesMaps;

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

                    case 'A':
                        matrix[ count ] = wallRelation.get('A');
                        break;

                    case 'B':
                        matrix[ count ] = wallRelation.get('B');
                        break;

                    case 'V':
                        matrix[ count ] = wallRelation.get('V');
                        break;
                    case 'D':
                        matrix[ count ] = wallRelation.get('D');
                        break;
                    case 'E':
                        matrix[ count ] = wallRelation.get('E');
                        break;
                    case 'F':
                        matrix[ count ] = wallRelation.get('F');
                        break;
                    case 'G':
                        matrix[ count ] = wallRelation.get('G');
                        break;
                    case 'H':
                        matrix[ count ] = wallRelation.get('H');
                        break;
                    case 'I':
                        matrix[ count ] = wallRelation.get('I');
                        break;
                    case 'J':
                        matrix[ count ] = wallRelation.get('J');
                        break;
                    case 'T':
                        matrix[ count ] = wallRelation.get('T');
                        break;
                    case 'U':
                        matrix[ count ] = wallRelation.get('U');
                        break;
                    case '>':
                        matrix[ count ] = wallRelation.get('>');
                        break;
                    case '<':
                        matrix[ count ] = wallRelation.get('<');
                        break;
                    case '+':
                        matrix[ count ] = wallRelation.get('+');
                        break;
                    case 'W':
                        matrix[ count ] = images[ 5 ];
                        break;
                }
                count++;
            }
        });

        previousMatrix = matrix;
        FillGameBoard();
    }

    /*
        Rules the physics movement of the player and move him in the gameBoard
     */
    public void Move ( int movement )
    {
        int oldPosition = currentPosition;
        try
        {
            if ( !contains(wallsTab,matrix[ currentPosition - movement ] ) && matrix[ currentPosition - movement ] != images[ 1 ])
            {
                if ( ( matrix[ currentPosition - movement ] == images[ 4 ] || matrix[ currentPosition - movement ] == images[ 5 ]  ) && ( matrix[ currentPosition -movement * 2 ] == images[ 2 ] || matrix[ currentPosition - movement * 2 ] == images[ 3 ] ))
                {
                    //check if a box is on the edge of the map
                    if ((!leftLimits.contains(currentPosition - movement) &&  - movement == -1) || (!rightLimits.contains(currentPosition - movement) && - movement == 1) ||  - movement != -1 &&  - movement != 1) {
                        //check if the player is on the edge of the map
                        if ((!leftLimits.contains(currentPosition) &&  - movement == -1) || (!rightLimits.contains(currentPosition) && - movement == 1) ||  - movement != -1 &&  - movement != 1)
                        {
                            previousCaseTemp = caseTemp;
                            previousMatrix = Arrays.copyOf(matrix, matrix.length);
                            listHistoryMap.put(previousMatrix,previousCaseTemp);
                            matrix[currentPosition] = caseTemp;
                            caseTemp = ( matrix[currentPosition - movement] == images[2] || matrix[currentPosition - movement] == images[3] ? caseTemp = matrix[currentPosition - movement] :  matrix[currentPosition - movement] == images[5] ? images[3] : images[2]) ;

                            if (matrix[currentPosition - movement] == images[5] )
                            {
                                matrix[currentPosition - movement ] = images[6];
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
                                matrix[currentPosition] = ( matrix[currentPosition] == images[5] ? images[6] : images[0] );
                            }
                            else if ( matrix[currentPosition - movement] == images[4] )
                            {
                                matrix[currentPosition - movement * 2] = images[4];
                                currentPosition -= movement;
                                matrix[currentPosition] = ( matrix[currentPosition] == images[5] ? images[6] : images[0] );
                            }
                            else
                            {
                                currentPosition -= movement;
                                matrix[currentPosition] = ( matrix[currentPosition] == images[3] ? images[6] : images[0] );
                            }
                        }
                    }
                }
                else if ( matrix[ currentPosition - movement ] != images[ 4 ] &&  matrix[ currentPosition - movement ] != images[ 5 ])
                {
                    // check if the player is on the edge of the map
                    if ((!leftLimits.contains(currentPosition) &&  - movement == -1) || (!rightLimits.contains(currentPosition) && - movement == 1) ||  - movement != -1 &&  - movement != 1)
                    {
                        CountObjectOnBoard();
                        previousCaseTemp = caseTemp;
                        previousMatrix = Arrays.copyOf(matrix, matrix.length);
                        listHistoryMap.put(previousMatrix,previousCaseTemp);

                        matrix[currentPosition] = caseTemp;

                        caseTemp = ( matrix[currentPosition - movement] == images[2] || matrix[currentPosition - movement] == images[3] ? matrix[currentPosition - movement] :  matrix[currentPosition - movement] == images[5] ? images[3] : images[2]) ;

                        currentPosition -= movement;

                        matrix[currentPosition] = ( matrix[currentPosition] == images[3] ? images[6] : images[0] );
                    }
                }

                if (oldPosition != currentPosition){ moveCount++; }
                textMove.setText(String.valueOf(moveCount));
                FillGameBoard();

                System.out.println("NbBox :" + nbBoxPlaced + " count : " + countNbBox);

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
                    int score = ScoreCount();
                    if ( loadScore < score )
                    {
                        SharedPref.SaveLevelScore(this, myMap.getIdMap(),score);
                    }

                    CallPopUpEndGame(score);
                }
                else if ( nbBoxPlaced == countNbBox && tuto  )
                {
                    finish();
                }
            }
        }
        catch ( Exception e) {
            System.out.println("Error : " + e);
        }

    }

    /*
        Retrieve all cases that are the limit of the map to restrict movements
        Store those values in 2 list left and right unaccepted movement
     */
    public void GetMapLimits()
    {
        leftLimits = new ArrayList<>();
        rightLimits = new ArrayList<>();

        for( int i = 0; i < ( myMap.getNbColumns() * myMap.getNbRows() ); i++ )
        {
            double currentLine = Math.ceil( i / myMap.getNbColumns() );

            if (i % myMap.getNbColumns() == 0 )
            {
                leftLimits.add( i );
            }
            else if ( ( i - currentLine ) % ( myMap.getNbColumns() - 1 )  == 0 )
            {
                rightLimits.add( i );
            }
        }
    }

    /*
        Display pop up with text to guide the player on in first time on the game
     */
    public void TutorialGuide()
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
            Move( 1);
            Handler handler = new Handler();
            handler.postDelayed(() -> {

                Move( 1);
                ArrayList<String> text = new ArrayList<>();
                text.add(" You need to be careful there are walls! ");
                text.add(" Come on, let's bring this aztaroth back to its cage and quickly !");
                CallPopUp(text);

            }, 500);
        }
        else if ( currentStepTuto == 2 )
        {
            Move( - myMap.getNbColumns());
            Handler handler = new Handler();
            tuto = false;
            handler.postDelayed(() -> {

                Move( - myMap.getNbColumns());
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

    /*
        Call pop up dialog with the text in parameter
     */
    public void CallPopUp( ArrayList<String> text  )
    {
        TutoDesign popup = new TutoDesign(this,R.layout.popup_tuto,text);
        Dialog myDialog = new Dialog(this);
        myDialog.setCanceledOnTouchOutside(false);

        listener = event -> RefreshTextPopUp(myDialog,popup,text);
        popup.changes.addPropertyChangeListener(listener);

        Window window = myDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        myDialog.setContentView( popup.getView( null ) );
        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog.show();
    }

    /*
        Display a pop up at the end of the level to go to next level or quit or replay
     */
    public void CallPopUpEndGame(int nbStar)
    {
        EndGame popup = new EndGame(this,R.layout.popup_end_game,nbStar);
        Dialog myDialog = new Dialog(this);
        myDialog.setCanceledOnTouchOutside(false);

        final boolean[] replay = {false};
        final boolean[] next = {false};

        Window window = myDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        myDialog.setContentView( popup.getView( null ) );

        popup.getReplayGame().setOnClickListener(var -> {
            replay[0] = true;
            RefreshGame();
            myDialog.dismiss();
        });

        popup.getNextLevel().setOnClickListener(var -> {
            next[0] = true;
            myDialog.dismiss();
            MapDAO.GetNextMap(this,myMap.getIdMap() );

        });

        popup.getReturn_back().setOnClickListener(var -> finish());

        myDialog.setOnDismissListener(var -> {

            if ( !replay[0] && !next[0])
            {
                HashMap params = new HashMap<>();
                Navigation.switchActivities(this, SelectActivity.class,params);
            }

        });

        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog.show();
    }

    /*
        Refresh all the level with the map of the class
     */
    public void RefreshGame()
    {
        moveCount = 0;
        nbBoxPlaced = 0;
        textMove.setText("0");
        view_text_level.setText(myMap.getNom());
        currentStepTuto = 0;
        caseTemp = images[2];
        ResponseMapLine( linesMapsTemp);
    }

    /*
        Change the current text which is display on the pop up in parameter with the list of text
        also in parameter
     */
    public void RefreshTextPopUp( Dialog myDialog, TutoDesign popup , ArrayList<String> text )
    {
        if (popup.listenerActive) {

            if ( popup.GetCurrentText() == text.size() )
            {
                myDialog.dismiss();
                currentStepTuto++;
                TutorialGuide();
            }
            else
            {
                myDialog.setContentView( popup.getView( null ) );
                myDialog.show();
                popup.listenerActive=false;
            }
        }
    }

    /*
        Called after retrieving the next map to play
        Refresh the level with the new map in parameter
     */
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
            GetMapLimits();
        });
    }

    static public boolean contains(int[] T,int val){
        return Arrays.toString(T).contains(String.valueOf(val));
    }

    /*
        Set the the current matrix equals to matrix - 1 and reset variables
     */
    public void RollBackAction()
    {
        if ( moveCount > 0 && previousMatrix != matrix)
        {
            moveCount--;
            textMove.setText(String.valueOf(moveCount));
        }

        List<int[]> listKeys = new ArrayList<>(listHistoryMap.keySet());

        if(listKeys.size() > 0 )
        {
            matrix = listKeys.get(listKeys.size() - 1);
            caseTemp = previousCaseTemp;
            try
            {
                previousMatrix = listKeys.get(listKeys.size() - 2);
            }
            catch(Exception e)
            {
                System.out.println("Error " + e );
            }

            previousCaseTemp = listHistoryMap.get(listKeys.get(listKeys.size() - 1));
            listHistoryMap.remove(listKeys.get(listKeys.size() - 1));
        }
        CountObjectOnBoard();
        FillGameBoard();

    }

    /*
     Count Object on the Game Board
     */

    public void CountObjectOnBoard()
    {
        nbBoxPlaced = 0;

        int countTemp = 0;

        for (int j : matrix)
        {
            if ( j == images[0] || j == images[6] )
            {
                currentPosition = countTemp;
            }
            else if ( j == images[5] )
            {
                nbBoxPlaced++;
            }
            countTemp++;
        }
    }

    public int ScoreCount(){
        int score = (moveCount*100)/ myMap.getNbMoveMin();
        int result;
        if(score <= 100){
            result = 3;
        }
        else if (score <= 133){
            result = 2;
        }
        else if (score <=166){
            result = 1;
        }
        else{
            result = 0;
        }
        return(result);
    }
}