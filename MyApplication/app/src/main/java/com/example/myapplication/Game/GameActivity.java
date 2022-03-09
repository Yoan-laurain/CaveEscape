package com.example.myapplication.Game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.Dto.MapLine;
import com.example.myapplication.Lib.GameDesign;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GameActivity extends AppCompatActivity
{
    GridView gameBoard;
    Map myMap;
    Map resetMap;
    int[] images = {R.drawable.perso,R.drawable.mur,R.drawable.sol,R.drawable.arrivee,R.drawable.boite,R.drawable.caisse_verte};
    private int[] matrix;
    private int count;
    ImageButton left;
    ImageButton right;
    ImageButton up;
    ImageButton down;
    ImageButton restart;
    private int currentPosition = 0;
    private int countNbBox;
    private int nbBoxPlaced;
    private int caseTemp = images[2];
    ArrayList<Integer> leftLimits = new ArrayList<>();
    ArrayList<Integer> rightLimits = new ArrayList<>();
    private boolean comingFromTest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //---------------------------Retrieve parameters----------------------- //

        myMap = (Map) getIntent().getSerializableExtra("Map");
        resetMap = myMap;
        Bundle args = new Bundle();
        comingFromTest = args.getBoolean("comingFromTest");
        MapDAO.GetMap( this,null, String.valueOf( myMap.getIdMap() ) );

        //-------------------------------------------------------------------- //

        //---------------------- Tool selector -------------------------------- //

        left = findViewById(R.id.button_game_left);
        right = findViewById(R.id.button_game_right);
        up = findViewById(R.id.button_game_up);
        down = findViewById(R.id.button_game_down);
        restart = findViewById(R.id.button_game_reload);

        //-------------------------------------------------------------------- //


        //---------------------- Set clicks actions -------------------------- //

        left.setOnClickListener(var ->  move( 1) );
        right.setOnClickListener(var ->move(- 1) );
        up.setOnClickListener(var -> move( myMap.getNbColumns() ) );
        down.setOnClickListener(var -> move( - myMap.getNbColumns() ) );
        getMapLimits();
        restart.setOnClickListener(var -> MapDAO.GetMap( this,null, String.valueOf( resetMap.getIdMap() ) ));

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

        List<MapLine> linesMapSorted = new ArrayList(lesLinesMaps.values());
        linesMapSorted.sort(Comparator.comparing(MapLine::getIndexRow));

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
                                nbBoxPlaced--;

                                matrix[currentPosition - movement ] = images[0];
                                matrix[currentPosition - movement * 2] = images[4];
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

                FillGameBoard();

                if ( nbBoxPlaced == countNbBox && comingFromTest )
                {
                    this.finish();
                }
                else if ( nbBoxPlaced == countNbBox)
                {
                    myMap.setIsTested(true);
                    MapDAO.UpdateIsTestMap( myMap );
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
    
}