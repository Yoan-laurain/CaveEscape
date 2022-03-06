package com.example.myapplication.Sandbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.Dto.MapLigne;
import com.example.myapplication.Lib.GameDesign;
import com.example.myapplication.MainMenu.LoadingActivity;
import com.example.myapplication.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class SandboxActivity extends AppCompatActivity
{
    ImageButton player;
    ImageButton box;
    ImageButton floor;
    ImageButton wall;
    ImageButton finish;

    EditText mapName;

    Button saveButton;
    Button deleteButton;

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
    int nbPlayerPlaced;
    int nbBoxPlaced;
    int positionPlayer;

    boolean Modification = false;

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
        saveButton = findViewById(R.id.save_game);
        deleteButton = findViewById(R.id.deleteGame);
        mapName = findViewById(R.id.map_name);

        player.setOnClickListener(var -> currentTool = 0);
        wall.setOnClickListener(var -> currentTool = 1);
        floor.setOnClickListener(var -> currentTool = 2);
        finish.setOnClickListener(var -> currentTool = 3);
        box.setOnClickListener(var -> currentTool = 4);


        spinnerLines = findViewById(R.id.list_nb_lines);
        spinnerColumns = findViewById(R.id.list_nb_columns);
        gameBoard = findViewById(R.id.sandbox_gameBoard);
        gameBoard.setOnItemClickListener((parent, view, position, id) -> ClickOnBoard(position) );

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, listNumbers);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerLines.setAdapter(adapter);
        spinnerColumns.setAdapter(adapter);

        saveButton.setOnClickListener(var -> saveGame());


        myMap = (Map) getIntent().getSerializableExtra("Map");

        try
        {
            spinnerLines.setSelection( myMap.getNbRows() - 1 );
            spinnerColumns.setSelection( myMap.getNbColumns() - 1 );
            matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];

            deleteButton.setOnClickListener(var ->{
                MapDAO.DeleteMap(this, myMap.getIdMap());
                this.finish();
                });

            MapDAO.getMap( null,this, String.valueOf( myMap.getIdMap() ) );

            Modification = true;

        }
        catch(Exception e)
        {
            spinnerLines.setSelection(4);
            spinnerColumns.setSelection(4);
            myMap = new Map(0,"",5,5, LoadingActivity.idClient);
            matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];

            for ( int i = 0; i < myMap.getNbRows()*myMap.getNbColumns(); i++)
            {
                matrix[i] = images[2];
            }
            //Modification = false;
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

        List<MapLigne> linesMapSorted = new ArrayList(lesLignesMaps.values());
        Collections.sort(linesMapSorted, Comparator.comparing(MapLigne::getIndexRow));

        linesMapSorted.forEach(MapLigne ->
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

    /*
        Called when the user try to save his map.
        Check if the map is quite correct and display toast otherwise.
     */
    public void saveGame()
    {
        System.out.println("Verif : " + MapIsClosed());
        System.out.println("Verif2 : " + PlayerInside());


        if ( mapName.getText().toString().length() == 0 )
        {
            Toast.makeText(this, "Fill the name section ! ", Toast.LENGTH_LONG).show();
        }
        else
        {
            CountObjectOnBoard();
            System.out.println("nbPLayer : " + nbPlayerPlaced);
            if ( nbPlayerPlaced == 1 )
            {
                if ( nbBoxPlaced > 0 )
                {
                    if ( EnoughFinishPlace() == true )
                    {

                        myMap.setName( mapName.getText().toString() );
                        System.out.println("Mode Ouverture Page = "  + Modification);
                        if (Modification == true) {
                            System.out.println("Modification de la map");
                            MapDAO.updateMap(this, myMap);
                        } else {
                            System.out.println("creation d'une nouvelle map");
                            MapDAO.saveMap(this, myMap);
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "You don't have enough end zone(s) for you're boxe(s)! ", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "You need at least one box. ", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(this, "You need to have one player! ", Toast.LENGTH_LONG).show();
            }
        }


    }

    /*
        Called after the save of a map ( header )  in the dataBase.
        Create mapLines object to save them too with the id of the map created
     */
    public void responseAfterSaveMap( int id )
    {
        int countNumber = 0;

        for ( int i = 0; i < myMap.getNbRows(); i++ )
        {
            String content = "";

            for ( int j = 0; j < myMap.getNbColumns(); j++ )
            {

                switch ( matrix[ countNumber ] )
                {
                    case R.drawable.perso :
                        content+= "P";
                        break;

                    case R.drawable.mur:
                        content+= "#";
                        break;

                    case R.drawable.sol:
                        content+= ".";
                        break;

                    case R.drawable.arrivee:
                        content+= "X";
                        break;

                    case R.drawable.boite:
                        content+= "C";
                        break;
                }

                countNumber++;
            }

            MapLigne myMapLine = new MapLigne( 0, i , content, id );

            MapDAO.saveMapLines(this, myMapLine);

        }
        finish();
    }


    public void responseAfterUpdateMap()
    {
        int countNumber = 0;

        for ( int i = 0; i < myMap.getNbRows(); i++ )
        {
            String content = "";

            for ( int j = 0; j < myMap.getNbColumns(); j++ )
            {

                switch ( matrix[ countNumber ] )
                {
                    case R.drawable.perso :
                        content+= "P";
                        break;

                    case R.drawable.mur:
                        content+= "#";
                        break;

                    case R.drawable.sol:
                        content+= ".";
                        break;

                    case R.drawable.arrivee:
                        content+= "X";
                        break;

                    case R.drawable.boite:
                        content+= "C";
                        break;
                }

                countNumber++;
            }

            MapLigne myMapLine = new MapLigne( 0, i , content,  myMap.getIdMap());

            MapDAO.updateMapLines(this, myMapLine);

        }
        finish();
    }



    /*
        Check if there is enough finish place for the number of boxes placed.
     */
    public boolean EnoughFinishPlace ()
    {
        int nbBox = 0;
        int nbFinishZone = 0;

        for ( int i = 0 ; i < matrix.length; i++ )
        {
            if ( matrix[i] == images[3] )
            {
                nbFinishZone++;
            }
            else if ( matrix[i] == images[4] )
            {
                nbBox++;
            }
        }
        return ( nbBox <= nbFinishZone ? true : false );
    }

    /*
        Called when the user click on the map to place item.
        Call the method to resfresh the board.
     */
    public void ClickOnBoard(int position)
    {
        if ( matrix[position] == images[0] )
        {
            nbPlayerPlaced--;
        }
        else if ( matrix[position] == images[4] )
        {
            nbBoxPlaced--;
        }

        matrix[position] = images[currentTool];

        if ( images[currentTool] == images[0] )
        {
            positionPlayer = position;
            nbPlayerPlaced++;
        }
        else if ( images[currentTool] == images[4] )
        {
            nbBoxPlaced++;
        }
        FillGameBoard();
    }


    public boolean MapIsClosed()
    {
        int index = 0;
        boolean playerInside;
        //Pour chacune des lignes
        for ( int j = 0 ; j < myMap.getNbRows(); j++ )
        {

            //Pour chacunes des colonnes
            for (int i = 0; i < myMap.getNbColumns(); i++ )
            {
                boolean closedUp = false;
                boolean closedDown = false;
                boolean closedLeft = false;
                boolean closedRight = false;

                //Monte jusu'en haut
                for (int a = ( j + 1) * ( i + 1); a >= 0; a -= myMap.getNbColumns() )
                {
                    try
                    {
                        if ( matrix[ a ] == images[1] )
                        {
                            System.out.println("Closed up for : " + i );
                            closedUp = true;
                            break;
                        }
                    }
                    catch (Exception e){}

                }


                //Descend jusu'en bas
                for (int a = ( j + 1* myMap.getNbColumns() ) - 1; a < myMap.getNbRows() * myMap.getNbColumns(); a += myMap.getNbColumns() ) {
                    if (matrix[a] == images[1])
                    {
                        System.out.println("Closed down for : " + i);
                        closedDown = true;
                        break;
                    }
                }


                //Va jusu'a gauche
                for (int b = ( i  * j + 1 ); b >= 0; b-- )
                {
                    if ( matrix[ b ] == images[1] )
                    {
                        System.out.println("Closed left for : " + i );
                        closedLeft = true;
                        break;
                    }
                }


                //Va jusu'a droite
                for (int c = ( i + 2) * (j + 1); c < myMap.getNbColumns(); c++ )
                {
                    if ( matrix[ c ] == images[1] )
                    {
                        System.out.println("Closed right for : " + i );
                        closedRight = true;
                        break;
                    }
                }

                int countClose = ( !closedDown ? 0 : 1) +  ( !closedLeft ? 0 : 1) + ( !closedRight ? 0 : 1) + ( !closedUp ? 0 : 1);

                if (  matrix[ index ] == images[1] )
                {
                    System.out.println("TYO");
                    if ( countClose < 2 )
                    {
                        System.out.println("In pour j : " + j + " et i : " + i);
                        return false;
                    }
                }
                else
                {
                    System.out.println("Count : " + countClose);
                    if ( countClose < 3 )
                    {
                        System.out.println("In0 pour j : " + j + " et i : " + i);
                        return false;
                    }
                }


                index++;
            }



        }

        return true;
    }

    public boolean PlayerInside()
    {
        boolean closedUp = false;
        boolean closedDown = false;
        boolean closedLeft = false;
        boolean closedRight = false;

        //Check vers le haut
        for ( int j = positionPlayer ; j > 0 ; j -= myMap.getNbColumns() )
        {
            try {
                if ( matrix[j] == images[1] )
                {
                    closedUp = true;
                }
            }
            catch (Exception e)
            {

            }
        }

        //Check vers le bas
        for ( int j = positionPlayer ; j < myMap.getNbRows()* myMap.getNbColumns() ; j += myMap.getNbColumns() )
        {
            try {
                if ( matrix[j] == images[1] )
                {
                    closedDown = true;
                }
            }
            catch (Exception e)
            {

            }
        }

        //Check vers la gauche
        for ( int j = positionPlayer % myMap.getNbColumns(); j >= 0 ; j -- )
        {
            try {
                if ( matrix[j] == images[1] )
                {
                    closedLeft = true;
                }
            }
            catch (Exception e){}
        }

        //Check vers la droite
        for ( int j = positionPlayer % myMap.getNbColumns(); j <= myMap.getNbColumns() ; j ++ )
        {
            try {
                if ( matrix[j] == images[1] )
                {
                    closedRight = true;
                }
            }
            catch (Exception e){}
        }

        return closedDown && closedLeft && closedUp && closedRight;

    }




    public void CountObjectOnBoard(){
        nbPlayerPlaced = 0;
        nbBoxPlaced = 0;

        for ( int i = 0 ; i < matrix.length; i++ )
        {
            if ( matrix[i] == images[0] )
            {
                nbPlayerPlaced++;
            }
            else if ( matrix[i] == images[4] )
            {
                nbBoxPlaced++;
            }
        }
    }
}