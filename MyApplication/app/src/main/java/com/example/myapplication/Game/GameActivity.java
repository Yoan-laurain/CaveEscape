package com.example.myapplication.Game;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    int images[] = {R.drawable.perso,R.drawable.mur,R.drawable.sol,R.drawable.arrivee,R.drawable.boite,R.drawable.caisse_verte};
    private int[] matrix;
    private int count;
    ImageButton left;
    ImageButton right;
    ImageButton up;
    ImageButton down;
    private int currentPosition = 0;
    private int countNbBox;
    private int nbBoxPlaced;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        myMap = (Map) getIntent().getSerializableExtra("Map");
        MapDAO.getMap( this, String.valueOf( myMap.getIdMap() ) );

        left = findViewById(R.id.button_game_left);
        left.setOnClickListener(var ->  move( 1) );

        right = findViewById(R.id.button_game_right);
        right.setOnClickListener(var ->move(- 1) );

        up = findViewById(R.id.button_game_up);
        up.setOnClickListener(var -> move( myMap.getNbColumns() ) );

        down = findViewById(R.id.button_game_down);
        down.setOnClickListener(var -> move( - myMap.getNbColumns() ) );

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
        if ( matrix[ currentPosition - movement ] != images[ 1 ])
        {
            //Si c'est la boîte et que la case suivante est l'arrivée ou un sol
            if ( ( matrix[ currentPosition - movement ] == images[ 4 ] || matrix[ currentPosition - movement ] == images[ 5 ]  ) && ( matrix[ currentPosition -movement * 2 ] == images[ 2 ] || matrix[ currentPosition - movement * 2 ] == images[ 3 ] ))
            {
                //On vérifie si la case ou on pousse la caisse est une zone d'arrivée
                if ( matrix[ currentPosition - movement * 2 ] == images[3] )
                {
                    //Si la caisse qu'on pousse était déjà bine placée
                    if ( matrix[ currentPosition - movement * 1 ] == images[5]  )
                    {
                        nbBoxPlaced--;
                    }

                    //On repositionne la boite verte sur 2 cases après
                    matrix[ currentPosition - movement * 2 ] = images[5];
                    nbBoxPlaced++;
                }
                else
                {
                    //On repositionne la boite sur 2 cases après
                    matrix[ currentPosition - movement * 2 ] = images[4];
                }

                //On remplace le joueur par un sol
                matrix[currentPosition] = images[2];
                //Bouge le perso
                currentPosition -= movement;
                //Place l'image du perso
                matrix[currentPosition] = images[0];

            }//Si c'est pas la boite
            else if ( matrix[ currentPosition - movement ] != images[ 4 ] &&  matrix[ currentPosition - movement ] != images[ 5 ])
            {
                //On remplace le joueur par un sol
                matrix[currentPosition] = images[2];
                //Bouge le perso
                currentPosition -= movement;
                //Place l'image du perso
                matrix[currentPosition] = images[0];
            }
            //Refresh le plateau
            FillGameBoard();

            if ( nbBoxPlaced == countNbBox )
            {
                this.finish();
            }

        }
    }

}