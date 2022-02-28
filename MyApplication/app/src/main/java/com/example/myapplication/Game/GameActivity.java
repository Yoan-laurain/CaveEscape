package com.example.myapplication.Game;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    Button left;
    Button right;
    Button up;
    Button down;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        myMap = (Map) getIntent().getSerializableExtra("Map");
        MapDAO.getMap( this, String.valueOf( myMap.getIdMap() ) );

        left = findViewById(R.id.button_game_left);
        left.setOnClickListener(var -> mouvementLeft() );

        right = findViewById(R.id.button_game_right);
        right.setOnClickListener(var -> mouvementRight() );

        up = findViewById(R.id.button_game_up);
        up.setOnClickListener(var -> mouvementUp() );

        down = findViewById(R.id.button_game_down);
        down.setOnClickListener(var -> mouvementDown() );

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
                        break;
                }
                count++;
            }
        });

        FillGameBoard();
    }

    public void mouvementLeft()
    {
        //Si la case est pas une caisse
        if ( matrix[ currentPosition - 1 ] != images[ 1 ])
        {
            //Si c'est la boîte et que la case suivante est l'arrivée ou un sol
            if ( matrix[ currentPosition - 1 ] == images[ 4 ] && ( matrix[ currentPosition - 2 ] == images[ 2 ] || matrix[ currentPosition - 2 ] == images[ 3 ] ))
            {
                //On repositionne la boite sur 2 cases après
                matrix[ currentPosition - 2 ] = images[4];
                //On remplace le joueur par un sol
                matrix[currentPosition] = images[2];
                //Bouge le perso
                currentPosition --;
                //Place l'image du perso
                matrix[currentPosition] = images[0];
            }//Si c'est pas la boite
            else if ( matrix[ currentPosition - 1 ] != images[ 4 ])
            {
                //On remplace le joueur par un sol
                matrix[currentPosition] = images[2];
                //Bouge le perso
                currentPosition --;
                //Place l'image du perso
                matrix[currentPosition] = images[0];
            }

            //refresh le plateau
            FillGameBoard();
        }

    }

    public void mouvementRight()
    {
        if ( matrix[ currentPosition + 1 ] != images[ 1 ])
        {
            //Si c'est la boîte et que la case suivante est l'arrivée ou un sol
            if ( matrix[ currentPosition + 1 ] == images[ 4 ] && ( matrix[ currentPosition + 2 ] == images[ 2 ] || matrix[ currentPosition + 2 ] == images[ 3 ] ))
            {
                //On repositionne la boite sur 2 cases après
                matrix[ currentPosition + 1 ] = images[4];
                //On remplace le joueur par un sol
                matrix[currentPosition] = images[2];
                //Bouge le perso
                currentPosition ++;
                //Place l'image du perso
                matrix[currentPosition] = images[0];
            }//Si c'est pas la boite
            else if ( matrix[ currentPosition + 1 ] != images[ 4 ])
            {
                //On remplace le joueur par un sol
                matrix[currentPosition] = images[2];
                //Bouge le perso
                currentPosition ++;
                //Place l'image du perso
                matrix[currentPosition] = images[0];
            }
            FillGameBoard();
        }

    }

    public void mouvementUp()
    {
        if ( matrix[ currentPosition - myMap.getNbColumns() ] != images[ 1 ])
        {

            //Si c'est la boîte et que la case suivante est l'arrivée ou un sol
            if ( matrix[ currentPosition - myMap.getNbColumns() ] == images[ 4 ] && ( matrix[ currentPosition - myMap.getNbColumns() * 2 ] == images[ 2 ] || matrix[ currentPosition - myMap.getNbColumns() * 2 ] == images[ 3 ] ))
            {
                //On repositionne la boite sur 2 cases après
                matrix[ currentPosition - myMap.getNbColumns() * 2 ] = images[4];
                //On remplace le joueur par un sol
                matrix[currentPosition] = images[2];
                //Bouge le perso
                currentPosition -= myMap.getNbColumns();
                //Place l'image du perso
                matrix[currentPosition] = images[0];
            }//Si c'est pas la boite
            else if ( matrix[ currentPosition - myMap.getNbColumns() ] != images[ 4 ])
            {
                //On remplace le joueur par un sol
                matrix[currentPosition] = images[2];
                //Bouge le perso
                currentPosition -= myMap.getNbColumns();
                //Place l'image du perso
                matrix[currentPosition] = images[0];
            }
            FillGameBoard();
        }

    }

    public void mouvementDown()
    {
        //Si la case est pas une caisse
        if ( matrix[ currentPosition + myMap.getNbColumns() ] != images[ 1 ])
        {
            //Si c'est la boîte et que la case suivante est l'arrivée ou un sol
            if ( matrix[ currentPosition + myMap.getNbColumns() ] == images[ 4 ] && ( matrix[ currentPosition + myMap.getNbColumns() * 2 ] == images[ 2 ] || matrix[ currentPosition + myMap.getNbColumns() * 2 ] == images[ 3 ] ))
            {
                //On repositionne la boite sur 2 cases après
                matrix[ currentPosition + myMap.getNbColumns() * 2 ] = images[4];
                //On remplace le joueur par un sol
                matrix[currentPosition] = images[2];
                //Bouge le perso
                currentPosition += myMap.getNbColumns();
                //Place l'image du perso
                matrix[currentPosition] = images[0];
            }//Si c'est pas la boite
            else if ( matrix[ currentPosition + myMap.getNbColumns() ] != images[ 4 ])
            {
                //On remplace le joueur par un sol
                matrix[currentPosition] = images[2];
                //Bouge le perso
                currentPosition += myMap.getNbColumns();
                //Place l'image du perso
                matrix[currentPosition] = images[0];
            }

            //refresh le plateau
            FillGameBoard();
        }

    }
}