package com.example.myapplication.Sandbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.Dto.MapLigne;
import com.example.myapplication.Lib.GameDesign;
import com.example.myapplication.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class SandboxActivity extends AppCompatActivity
{
    ImageButton player;
    ImageButton box;
    ImageButton floor;
    ImageButton wall;
    ImageButton finish;

    Spinner spinnerLines;
    Spinner spinnerColumns;
    ArrayList<Integer> listNumbers = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10) ) ;
    int images[] = {R.drawable.perso,R.drawable.mur,R.drawable.sol,R.drawable.arrivee,R.drawable.boite,R.drawable.caisse_verte};
    private int[] matrix;
    private int[] matrixTemp;
    private int count;
    GridView gameBoard;
    Map myMap;
    int currentTool = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

        player = findViewById(R.id.button_sandbox_perso);
        wall = findViewById(R.id.button_sandbox_mur);
        floor = findViewById(R.id.button_sandbox_sol);
        finish = findViewById(R.id.button_sandbox_arrive);
        box = findViewById(R.id.button_sandbox_box);

        player.setOnClickListener(var -> currentTool = 0);
        wall.setOnClickListener(var -> currentTool = 1);
        floor.setOnClickListener(var -> currentTool = 2);
        finish.setOnClickListener(var -> currentTool = 3);
        box.setOnClickListener(var -> currentTool = 4);


        spinnerLines = findViewById(R.id.list_nb_lines);
        spinnerColumns = findViewById(R.id.list_nb_columns);
        gameBoard = findViewById(R.id.sandbox_gameBoard);
        gameBoard.setOnItemClickListener((parent, view, position, id) ->
                {
                    matrix[position] = images[currentTool];
                    FillGameBoard();
                });

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, listNumbers);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerLines.setAdapter(adapter);
        spinnerColumns.setAdapter(adapter);

        myMap = (Map) getIntent().getSerializableExtra("Map");


        if ( myMap != null )
        {
            spinnerLines.setSelection( myMap.getNbRows() - 1 );
            spinnerColumns.setSelection( myMap.getNbColumns() - 1 );
            matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];

            MapDAO.getMap( null,this, String.valueOf( myMap.getIdMap() ) );
        }
        else
        {
            spinnerLines.setSelection(4);
            spinnerColumns.setSelection(4);
            myMap = new Map(0,"",5,5,0);
            matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];

            for ( int i = 0; i < myMap.getNbRows()*myMap.getNbColumns(); i++)
            {
                matrix[i] = images[2];
            }
        }


        spinnerLines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                int nbLinesTemp = myMap.getNbRows();

                myMap.setNbRows(position + 1);
                matrixTemp = matrix;

                matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];

                for ( int i = 0; i < matrix.length; i++)
                {
                    //Si on a ajouté une ou plusieurs lignes
                    if ( myMap.getNbRows() > nbLinesTemp )
                    {
                        if ( i >= nbLinesTemp * myMap.getNbColumns()  )
                        {
                            matrix[i] = images[2];
                        }
                        else
                        {
                            matrix[i] = matrixTemp[i];
                        }

                    } //Si on a retiré une ou plusieurs lignes
                    else if ( myMap.getNbRows() > nbLinesTemp )
                    {
                        if ( i <= myMap.getNbRows() * myMap.getNbColumns()  )
                        {
                            matrix[i] = matrixTemp[i];
                        }

                    }
                    else
                    {
                        matrix[i] = matrixTemp[i];
                    }

                }

                FillGameBoard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinnerColumns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {

                int nbColumnTemp = myMap.getNbColumns();

                myMap.setNbColumns(position + 1);
                matrixTemp = matrix;

                matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];
                int countTemp = 0;

                int max = ( matrixTemp.length > myMap.getNbColumns() *  myMap.getNbRows() ? matrixTemp.length :  myMap.getNbColumns() *  myMap.getNbRows() );

                for ( int i = 0; i < max ;i++)
                {
                    //Si on a ajouté une ou plusieurs colonnes
                    if ( myMap.getNbColumns() > nbColumnTemp )
                    {
                        if ( i % myMap.getNbColumns() >= nbColumnTemp   )
                        {
                            matrix[i] = images[2];
                        }
                        else
                        {
                            matrix[i] = matrixTemp[countTemp];
                            countTemp++;
                        }

                    } //Si on a retiré une ou plusieurs colonnes
                    else if ( myMap.getNbColumns() < nbColumnTemp )
                    {

                        if ( countTemp < myMap.getNbColumns() * myMap.getNbRows() )
                        {
                            if (countTemp % (myMap.getNbColumns()) == 0 && i != 0)
                            {
                                i += nbColumnTemp - myMap.getNbColumns();
                                matrix[countTemp] = matrixTemp[i];
                                countTemp++;
                            } else
                            {
                                matrix[countTemp] = matrixTemp[i];
                                countTemp++;
                            }
                        }
                    }
                    else
                    {
                        matrix[i] = matrixTemp[i];
                    }

                }

                FillGameBoard();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        FillGameBoard();
    }

    /*
    Fill the gameBoard with the number of item of the matrix
 */
    public void FillGameBoard()
    {
        this.runOnUiThread(() ->
        {
            gameBoard.setColumnWidth( myMap.getNbColumns() * 4 );
            gameBoard.setNumColumns( myMap.getNbColumns() );

            GameDesign adapter = new GameDesign(this, images, matrix, myMap.getNbRows() * 30 );
            gameBoard.setAdapter(adapter);

        });
    }

    /*
    Called after the response of the API
 */
    public void responseMapLigne( HashMap<Integer, MapLigne> lesLignesMaps )
    {
        matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];
        count = 0;

        lesLignesMaps.values().forEach(MapLigne ->
        {
            for (int i = 0; i < MapLigne.getContent().length(); i++)
            {
                switch ( MapLigne.getContent().charAt(i) )
                {
                    case 'P' :
                        matrix[ count ] = images[ 0 ];
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
                        break;
                }
                count++;
            }
        });

        FillGameBoard();
    }

}