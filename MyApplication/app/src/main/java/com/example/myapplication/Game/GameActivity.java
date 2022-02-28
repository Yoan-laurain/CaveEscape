package com.example.myapplication.Game;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.Dto.MapLigne;
import com.example.myapplication.Lib.GameDesign;
import com.example.myapplication.R;

import java.util.HashMap;

public class GameActivity extends AppCompatActivity
{
    GridView gameBoard;
    Map myMap;
    int images[] = {R.drawable.perso,R.drawable.mur,R.drawable.sol,R.drawable.arrivee,R.drawable.boite};
    private int[] matrix;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        myMap = (Map) getIntent().getSerializableExtra("Map");
        MapDAO.getMap( this, String.valueOf( myMap.getIdMap() ) );

    }

    /*
        Fill the gameBoard with the number of item of the matrix
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
            gameBoard.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(getApplicationContext(), "You clicked ", Toast.LENGTH_SHORT).show());

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