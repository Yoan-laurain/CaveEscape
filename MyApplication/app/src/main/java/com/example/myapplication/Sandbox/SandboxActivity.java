package com.example.myapplication.Sandbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Dto.Map;
import com.example.myapplication.Lib.GameDesign;
import com.example.myapplication.R;
import java.util.ArrayList;
import java.util.Arrays;


public class SandboxActivity extends AppCompatActivity
{
    Spinner spinnerLines;
    Spinner spinnerColumns;
    ArrayList<Integer> listNumbers = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10) ) ;
    int images[] = {R.drawable.perso,R.drawable.mur,R.drawable.sol,R.drawable.arrivee,R.drawable.boite,R.drawable.caisse_verte};
    private int[] matrix;
    private int[] matrixTemp;
    GridView gameBoard;
    Map myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

        spinnerLines = findViewById(R.id.list_nb_lines);
        spinnerColumns = findViewById(R.id.list_nb_columns);
        gameBoard = findViewById(R.id.sandbox_gameBoard);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLines.setAdapter(adapter);
        spinnerColumns.setAdapter(adapter);

        spinnerLines.setSelection(4);
        spinnerColumns.setSelection(4);

        myMap = new Map(0,"",5,5,0);
        matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];

        for ( int i = 0; i < myMap.getNbRows()*myMap.getNbColumns(); i++)
        {
            matrix[i] = images[2];
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

                for ( int i = 0; i < matrix.length; i++)
                {
                    //Si on a ajouté une ou plusieurs colonnes
                    if ( myMap.getNbColumns() > nbColumnTemp )
                    {
                        if ( i % myMap.getNbColumns() >= myMap.getNbColumns() - nbColumnTemp )
                        {
                            matrix[i] = images[2];
                            countTemp++;
                        }
                        else
                        {
                            matrix[i] = matrixTemp[countTemp];

                        }

                    } //Si on a retiré une ou plusieurs colonnes
                    else if ( myMap.getNbColumns() < nbColumnTemp )
                    {
                        if ( i % myMap.getNbColumns() == 0  )
                        {
                            matrix[i] = matrixTemp[countTemp];
                        }
                        else
                        {
                            matrix[i] = matrixTemp[i];
                            countTemp++;
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
            gameBoard.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(getApplicationContext(), "You clicked ", Toast.LENGTH_SHORT).show());

        });
    }
}