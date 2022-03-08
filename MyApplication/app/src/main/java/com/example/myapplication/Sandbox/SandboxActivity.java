package com.example.myapplication.Sandbox;

import android.annotation.SuppressLint;
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
import com.example.myapplication.Dto.MapLine;
import com.example.myapplication.Game.GameActivity;
import com.example.myapplication.Lib.GameDesign;
import com.example.myapplication.Lib.Navigation;
import com.example.myapplication.MainMenu.LoadingActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
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
    Button testButton;

    Spinner spinnerLines;
    Spinner spinnerColumns;
    ArrayList<Integer> listNumbers = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10) ) ;
    int[] images = {R.drawable.perso,R.drawable.mur,R.drawable.sol,R.drawable.arrivee,R.drawable.boite,R.drawable.caisse_verte};
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

        //////////////////////////////////////////////////////////////////////////////////
        //                              Variables                                       //
        //////////////////////////////////////////////////////////////////////////////////

        // -------------------- Retrieving Visual Elements --------------------------- //
        player = findViewById(R.id.button_sandbox_perso);
        wall = findViewById(R.id.button_sandbox_mur);
        floor = findViewById(R.id.button_sandbox_sol);
        finish = findViewById(R.id.button_sandbox_arrive);
        box = findViewById(R.id.button_sandbox_box);
        saveButton = findViewById(R.id.save_game);
        deleteButton = findViewById(R.id.deleteGame);
        testButton = findViewById(R.id.testGame);
        mapName = findViewById(R.id.map_name);
        spinnerLines = findViewById(R.id.list_nb_lines);
        spinnerColumns = findViewById(R.id.list_nb_columns);
        gameBoard = findViewById(R.id.sandbox_gameBoard);


        // --------------------- Tool Selector --------------------------- //

        player.setOnClickListener(var -> currentTool = 0);
        wall.setOnClickListener(var -> currentTool = 1);
        floor.setOnClickListener(var -> currentTool = 2);
        finish.setOnClickListener(var -> currentTool = 3);
        box.setOnClickListener(var -> currentTool = 4);
        saveButton.setOnClickListener(var -> SaveGame());
        testButton.setOnClickListener(var -> TestGame());
        gameBoard.setOnItemClickListener((parent, view, position, id) -> ClickOnBoard(position) );

        // ---------------------- Adapters --------------------------- //

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, listNumbers);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerLines.setAdapter(adapter);
        spinnerColumns.setAdapter(adapter);

        //////////////////////////////////////////////////////////////////////////////////
        //                                      Code                                    //
        //////////////////////////////////////////////////////////////////////////////////

        myMap = (Map) getIntent().getSerializableExtra("Map");

        //----------------------------- Trying To Load a Map ---------------------------//
        try
        {
            spinnerLines.setSelection( myMap.getNbRows() - 1 );
            spinnerColumns.setSelection( myMap.getNbColumns() - 1 );
            matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];

            deleteButton.setOnClickListener(var ->{
                MapDAO.DeleteMap( myMap.getIdMap());
                this.finish();
                });

            MapDAO.GetMap( null,this, String.valueOf( myMap.getIdMap() ) );
            mapName.setText(myMap.getNom());
            Modification = true;

        }
        // ------------------------------- No map To load ------------------------------//
        catch(Exception e)
        {
            spinnerLines.setSelection(4);
            spinnerColumns.setSelection(4);
            myMap = new Map(0,"",5,5,false, LoadingActivity.idClient);
            matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];

            for ( int i = 0; i < myMap.getNbRows()*myMap.getNbColumns(); i++)
            {
                matrix[i] = images[2];
            }
            //Modification = false;
        }

        // --------------------------- Size Selector (Rows) --------------------------------------//
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

                    }
                    else if ( myMap.getNbRows() < nbLinesTemp )
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

        // --------------------------- Size Selector (Cols) --------------------------------------//
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
                    }
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

    // -------------------------- Fill the Game Board With The Matrix ----------------------------//
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
    public void responseMapLine(HashMap<Integer, MapLine> lineMaps )
    {
        matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];
        count = 0;

        List<MapLine> linesMapSorted = new ArrayList<>(lineMaps.values());
        linesMapSorted.sort(Comparator.comparing(MapLine::getIndexRow));

        linesMapSorted.forEach(MapLine ->
        {
            for (int i = 0; i < MapLine.getContent().length(); i++)
            {
                switch ( MapLine.getContent().charAt(i) )
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


    // ---------------- Called when the user try to save his map. --------------------------------//
    // ------------ Check if the map is quite correct and display toast otherwise. ---------------//
    public void SaveGame()
    {
        if ( mapName.getText().toString().length() == 0 )
        {
            Toast.makeText(this, "Fill the name section ! ", Toast.LENGTH_LONG).show();
        }
        else
        {
            CountObjectOnBoard();
            if ( nbPlayerPlaced == 1 ) {
                if (nbBoxPlaced > 0) {
                    if (EnoughFinishPlace())
                    {
                        myMap.setName(mapName.getText().toString());
                        if (Modification)
                        {
                            myMap.setIsTested(false);
                            MapDAO.updateMap(this, myMap);
                        } else
                        {
                            MapDAO.saveMap(this, myMap);
                        }

                    } else {
                        Toast.makeText(this, "You don't have enough end zone(s) for you're box(s)! ", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "You need at least one box. ", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(this, "You need at least one player. ", Toast.LENGTH_LONG).show();
            }
        }
    }


    // ---------- Called after the save of a map ( header )  in the dataBase. --------------------//
    //------ Create mapLines object to save them too with the id of the map created --------------//

    @SuppressLint("NonConstantResourceId")
    public void responseAfterSaveMap(int id )
    {
        int countNumber = 0;

        for ( int i = 0; i < myMap.getNbRows(); i++ )
        {
            StringBuilder content = new StringBuilder();

            for ( int j = 0; j < myMap.getNbColumns(); j++ )
            {

                switch ( matrix[ countNumber ] )
                {
                    case R.drawable.perso :
                        content.append("P");
                        break;

                    case R.drawable.mur:
                        content.append("#");
                        break;

                    case R.drawable.sol:
                        content.append(".");
                        break;

                    case R.drawable.arrivee:
                        content.append("X");
                        break;

                    case R.drawable.boite:
                        content.append("C");
                        break;
                }

                countNumber++;
            }

            MapLine myMapLine = new MapLine( 0, i , content.toString(), id );

            MapDAO.saveMapLines( myMapLine );

        }
        finish();
    }

    // --------------------- Called After API Response -------------------------------------------//
    @SuppressLint("NonConstantResourceId")
    public void responseAfterUpdateMap()
    {
        int countNumber = 0;

        for ( int i = 0; i < myMap.getNbRows(); i++ )
        {
            StringBuilder content = new StringBuilder();

            for ( int j = 0; j < myMap.getNbColumns(); j++ )
            {

                switch ( matrix[ countNumber ] )
                {
                    case R.drawable.perso :
                        content.append("P");
                        break;
                    case R.drawable.mur:
                        content.append("#");
                        break;
                    case R.drawable.sol:
                        content.append(".");
                        break;
                    case R.drawable.arrivee:
                        content.append("X");
                        break;
                    case R.drawable.boite:
                        content.append("C");
                        break;
                }
                countNumber++;
            }

            MapLine myMapLine = new MapLine( 0, i , content.toString(),  myMap.getIdMap());

            MapDAO.updateMapLines( myMapLine);

        }
        finish();
    }

     // ------- Check if there is enough finish place for the number of boxes placed. ------------//
    public boolean EnoughFinishPlace ()
    {
        int nbBox = 0;
        int nbFinishZone = 0;

        for (int j : matrix) {
            if (j == images[3]) {
                nbFinishZone++;
            } else if (j == images[4]) {
                nbBox++;
            }
        }
        return (nbBox <= nbFinishZone);
    }

    // ----------------- Called when the user click on the map to place item. --------------------//
    // ---------------------- Call the method to refresh the board. -----------------------------//

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
            if (nbPlayerPlaced != 0 ) {
                matrix[positionPlayer] = images[2];
            }

            positionPlayer = position;
            matrix[positionPlayer] = images[0];
            nbPlayerPlaced++;

        }
        else if ( images[currentTool] == images[4] )
        {
            nbBoxPlaced++;
        }
        FillGameBoard();
    }


    // ------------------------------ Count Object on the Game Board ---------------------------- //
    public void CountObjectOnBoard(){
        nbPlayerPlaced = 0;
        nbBoxPlaced = 0;

        for (int j : matrix) {
            if (j == images[0]) {
                nbPlayerPlaced++;
            } else if (j == images[4]) {
                nbBoxPlaced++;
            }
        }
    }

    // ------------------------------------------------------------------------------------------ //

    public void TestGame()
    {
        if ( myMap.getIdMap() != 0 ){
            HashMap params = new HashMap<>();
            params.put("Map", myMap);
            params.put("comingFromTest", true);

            Navigation.switchActivities(this, GameActivity.class, params);
        }
        else
        {
            Toast.makeText(this, "You need to save the map first !", Toast.LENGTH_SHORT).show();
        }


    }
}