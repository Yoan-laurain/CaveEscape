package com.example.myapplication.Game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    int[] images = {R.drawable.perso,R.drawable.mur,R.drawable.sol,R.drawable.arrivee,R.drawable.boite,R.drawable.caisse_verte};
    private int[] matrix;
    private int count;
    ImageButton left;
    ImageButton right;
    ImageButton up;
    ImageButton down;
    private int currentPosition = 0;
    private int countNbBox;
    private int nbBoxPlaced;
    private int caseTemp = images[2];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //---------------------------Retrieve parameters----------------------- //

        myMap = (Map) getIntent().getSerializableExtra("Map");
        MapDAO.GetMap( this,null, String.valueOf( myMap.getIdMap() ) );

        //-------------------------------------------------------------------- //

        //---------------------- Tool selector -------------------------------- //

        left = findViewById(R.id.button_game_left);
        right = findViewById(R.id.button_game_right);
        up = findViewById(R.id.button_game_up);
        down = findViewById(R.id.button_game_down);

        //-------------------------------------------------------------------- //


        //---------------------- Set clicks actions -------------------------- //

        left.setOnClickListener(var ->  move( 1) );
        right.setOnClickListener(var ->move(- 1) );
        up.setOnClickListener(var -> move( myMap.getNbColumns() ) );
        down.setOnClickListener(var -> move( - myMap.getNbColumns() ) );

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
                    if ( matrix[ currentPosition - movement * 2 ] == images[3] )
                    {
                        if ( matrix[ currentPosition - movement] == images[5]  )
                        {
                            nbBoxPlaced--;
                        }

                        matrix[ currentPosition - movement * 2 ] = images[5];
                        nbBoxPlaced++;
                    }
                    else
                    {
                        matrix[ currentPosition - movement * 2 ] = images[4];
                    }

                    matrix[currentPosition] = caseTemp;
                    currentPosition -= movement;
                    matrix[currentPosition] = images[0];

                }
                else if ( matrix[ currentPosition - movement ] != images[ 4 ] &&  matrix[ currentPosition - movement ] != images[ 5 ])
                {
                    matrix[currentPosition] = caseTemp;
                    currentPosition -= movement;

                    caseTemp = matrix[currentPosition];
                    matrix[currentPosition] = images[0];
                }

                FillGameBoard();

                if ( nbBoxPlaced == countNbBox )
                {
                    this.finish();
                }
            }
        }
        catch ( Exception ignored) {}
    }
}