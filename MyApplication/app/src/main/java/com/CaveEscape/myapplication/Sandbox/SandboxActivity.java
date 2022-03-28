package com.CaveEscape.myapplication.Sandbox;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.CaveEscape.myapplication.Dao.MapDAO;
import com.CaveEscape.myapplication.Dao.TextModeration;
import com.CaveEscape.myapplication.Dto.Map;
import com.CaveEscape.myapplication.Dto.MapLine;
import com.CaveEscape.myapplication.Game.GameActivity;
import com.CaveEscape.myapplication.Lib.GameDesign;
import com.CaveEscape.myapplication.Lib.TutoDesign;
import com.CaveEscape.myapplication.MainMenu.LoadingActivity;
import com.CaveEscape.myapplication.R;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SandboxActivity extends AppCompatActivity
{
    //----------------------------------------------------

    ImageButton player;
    ImageButton box;
    ImageButton floor;
    ImageButton wall;
    ImageButton finish;
    ImageButton monster_inside_cage;

    EditText mapName;

    Button saveButton;
    Button deleteButton;
    Button testButton;
    ImageView light;

    Spinner spinnerLines;
    Spinner spinnerColumns;

    GridView gameBoard;
    Map myMap;

    ImageView infoBubble;

    //----------------------------------------------------

    private ArrayList<Integer> wallChecked;
    private int currentTool = 2;
    private int count;
    private int nbPlayerPlaced;
    private int nbBoxPlaced;
    private int positionPlayer = -1;
    private int nbRowTemp;
    private int[] matrix;
    private int[] matrixTemp;
    int[] images = {R.drawable.left_player_blue,R.drawable.middle_vertical_wall_blue,R.drawable.blue_grass,R.drawable.opened_cage_blue,R.drawable.free_monster_blue,R.drawable.caged_monster_blue};

    private final int[] wallsTab = {R.drawable.bottom_left_angle_wall_blue,R.drawable.bottom_right_angle_wall_blue,
            R.drawable.bottom_vertical_wall_blue,R.drawable.middle_vertical_wall_blue,R.drawable.middle_straight_wall_blue,
            R.drawable.left_straight_wall_blue,R.drawable.right_straight_wall_blue,R.drawable.top_left_angle_wall_blue,
            R.drawable.top_right_angle_wall_blue,R.drawable.top_vertical_wall_blue,R.drawable.top_t_wall50,
            R.drawable.bottom_t_wall50, R.drawable.left_t_wall,R.drawable.right_t_wall,R.drawable.cross_wall};

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

    HashMap<Integer,Character> wallRelationForLoad = new HashMap<Integer,Character>(){{
        put(wallsTab[0] ,   'A');
        put(wallsTab[1] ,   'B');
        put(wallsTab[2] ,   'V');
        put(wallsTab[3] ,   'D');
        put(wallsTab[4] ,   'E');
        put(wallsTab[5] ,   'F');
        put(wallsTab[6] ,   'G');
        put(wallsTab[7] ,   'H');
        put(wallsTab[8] ,   'I');
        put(wallsTab[9] ,   'J');
        put(wallsTab[10],   'T');
        put(wallsTab[11],   'U');
        put(wallsTab[12],   '>');
        put(wallsTab[13],   '<');
        put(wallsTab[14],   '+');

    }};

    boolean textClean = true;
    boolean Modification = false;
    boolean mapIsInitialized = false;

    private ArrayList<Integer> leftLimits = new ArrayList<>();
    private ArrayList<Integer> rightLimits = new ArrayList<>();

    public PropertyChangeListener listener;


    ArrayList<Integer> listNumbers = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8) ) ;

    //----------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

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
        light = findViewById(R.id.lightIsTested);
        infoBubble = findViewById(R.id.info_bubble);
        monster_inside_cage = findViewById(R.id.button_sandbox_monster_inside_cage);

        // --------------------- Tool Selector --------------------------- //

        player.setOnClickListener(var -> currentTool = 0);
        wall.setOnClickListener(var -> currentTool = 1);
        floor.setOnClickListener(var -> currentTool = 2);
        finish.setOnClickListener(var -> currentTool = 3);
        box.setOnClickListener(var -> currentTool = 4);
        monster_inside_cage.setOnClickListener(var -> currentTool = 5);
        saveButton.setOnClickListener(var -> ScanText());
        testButton.setOnClickListener(var -> TestGame());
        gameBoard.setOnItemClickListener((parent, view, position, id) -> ClickOnBoard(position) );
        infoBubble.setOnClickListener(var-> TutorialGuide());



        // ---------------------- Adapters --------------------------- //

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, listNumbers);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerLines.setAdapter(adapter);
        spinnerColumns.setAdapter(adapter);

        //------------------------------------------------------------//

        // ---------------------- Retrieve parameters --------------------------- //

        myMap = (Map) getIntent().getSerializableExtra("Map");

        //------------------------------------------------------------//

        //----------------------------- Trying To Load a Map ---------------------------//
        try
        {
            spinnerLines.setSelection(myMap.getNbRows() - 1);
            spinnerColumns.setSelection(myMap.getNbColumns() - 1);

            matrix = new int[myMap.getNbColumns() * myMap.getNbRows()];

            deleteButton.setOnClickListener(var ->
            {
                MapDAO.DeleteMap(this,myMap.getIdMap());
                this.finish();
            });

            MapDAO.GetMap(null, this, String.valueOf(myMap.getIdMap()));
            mapName.setText(myMap.getNom());
            nbRowTemp = myMap.getNbRows();

            if ( myMap.getIsTested() )
            {
                light.setImageResource(R.drawable.green_circle);
            }
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
            light.setImageResource(R.drawable.red_circle);
        }


        // --------------------------- Size Selector (Rows) --------------------------------------//

        spinnerLines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {

                if ( position != myMap.getNbRows() - 1 )
                {
                    light.setImageResource(R.drawable.red_circle);
                    myMap.setIsTested(false);
                }

                int nbLinesTemp = myMap.getNbRows();

                myMap.setNbRows(position + 1);
                matrixTemp = matrix;

                matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];

                for ( int i = 0; i < matrix.length; i++)
                {
                    if ( myMap.getNbRows() > nbLinesTemp )
                    {
                        Modification = true;
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
                        Modification = true;
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
                FindPlayer();

                FillGameBoard();
                GetMapLimits();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // --------------------------- Size Selector (Cols) --------------------------------------//
        spinnerColumns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {

                if ( position != myMap.getNbColumns()-1 )
                {
                    light.setImageResource(R.drawable.red_circle);
                    myMap.setIsTested(false);
                }
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
                        Modification = true;
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
                        Modification = true;
                        if ( countTemp < myMap.getNbColumns() * myMap.getNbRows() )
                        {
                            if (countTemp % (myMap.getNbColumns()) == 0 && i != 0)
                            {
                                i += nbColumnTemp - myMap.getNbColumns();
                            }
                            matrix[countTemp] = matrixTemp[i];
                            countTemp++;
                        }
                    }
                    else
                    {
                        matrix[i] = matrixTemp[i];
                    }
                }
                FindPlayer();
                FillGameBoard();
                GetMapLimits();
                mapIsInitialized = true;
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

            int gameBoardHeight = gameBoard.getHeight() / myMap.getNbRows();
            //int gameBoardWidth = gameBoard.getWidth() / myMap.getNbColumns();

            // check the height of a line is good to display
            if(gameBoardHeight > 300){
                gameBoardHeight = 300;
                //params.height = myMap.getNbRows() * gameBoardHeight;
            }

            GameDesign adapter = new GameDesign(this, matrix, gameBoardHeight);
            gameBoard.setAdapter(adapter);

        });
    }

    /*
    Called after the response of the API
 */
    public void ResponseMapLine(HashMap<Integer, MapLine> lineMaps )
    {
        matrix = new int[ myMap.getNbColumns() * myMap.getNbRows() ];

        count = 0;

        List<MapLine> linesMapSorted = new ArrayList<>(lineMaps.values());
        linesMapSorted.sort(Comparator.comparing(MapLine::getIndexRow));

        linesMapSorted.forEach(MapLine ->
        {
            for (int i = 0; i < MapLine.getContent().length(); i++)
            {
                Integer value = wallRelation.get(MapLine.getContent().charAt(i));
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
                    case 'W':
                        matrix[ count ] = images[ 5 ];
                        break;
                    default :
                        matrix[ count ] = ( value != null ? value : images[2] );
                        break;
                }

                count++;
            }
        });

        FillGameBoard();
    }

    /*
        Check if the name not empty and then call api to verify his ethically correct
        hydrate boolean textClean with the value
     */
    public void ScanText()
    {
        if ( mapName.getText().toString().length() == 0 )
        {
            Toast.makeText(this, "Fill the name section ! ", Toast.LENGTH_LONG).show();
        }
        else
        {

            if ( mapName.getText().toString().length() > 20 )
            {
                Toast.makeText(this, "The name of the map can contain 20 characters max ! ", Toast.LENGTH_LONG).show();
            }
            else
            {
                TextModeration.ScanText(this,mapName.getText().toString());
            }


        }
    }

    /*
        Called when the user try to save his map.
        Check if the map is quite correct and display toast otherwise.
    */
    public void SaveGame()
    {
        CountObjectOnBoard();
        this.runOnUiThread(() ->
        {
            if (textClean) {
                if (nbPlayerPlaced == 1) {
                    if (nbBoxPlaced > 0) {
                        if (EnoughFinishPlace()) {
                            myMap.setName(mapName.getText().toString());
                            if (myMap.getIdMap() != 0) {
                                Modification = false;
                                MapDAO.updateMap(this, myMap);
                            } else {
                                Modification = false;
                                MapDAO.saveMap(this, myMap);
                            }
                            Toast.makeText(this, "Map saved ! ", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(this, "You don't have enough cage(s) for the aztharoth(s)! You need at least one empty cage", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "You need at least one cage. ", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "You need at least one player. ", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "The name of the map is unacceptable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
        Called after the response of the moderation language API
        Hydrate the boolean text clean with the response
        and call the Save Game method
     */
    public void ResponseTextModeration(Boolean response)
    {
        textClean = response;
        SaveGame();
    }

    /*
         Called after the save of a map ( header )  in the dataBase.
         Create mapLines object to save them too with the id of the map created
     */

    @SuppressLint("NonConstantResourceId")
    public void ResponseAfterSaveMap(int id )
    {
        int countNumber = 0;

        myMap.setIdMap(id);

        for ( int i = 0; i < myMap.getNbRows(); i++ )
        {
            StringBuilder content = new StringBuilder();

            for ( int j = 0; j < myMap.getNbColumns(); j++ )
            {
                switch ( matrix[ countNumber ] )
                {
                    case R.drawable.left_player_blue:
                        content.append("P");
                        break;

                    case R.drawable.mur:
                        content.append("#");
                        break;

                    case R.drawable.blue_grass:
                        content.append(".");
                        break;

                    case R.drawable.opened_cage_blue:
                        content.append("X");
                        break;

                    case R.drawable.free_monster_blue:
                        content.append("C");
                        break;

                    case R.drawable.caged_monster_blue:
                        content.append("W");
                        break;

                    case R.drawable.bottom_left_angle_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.bottom_left_angle_wall_blue));
                        break;

                    case R.drawable.bottom_right_angle_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.bottom_right_angle_wall_blue));
                        break;

                    case R.drawable.bottom_vertical_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.bottom_vertical_wall_blue));
                        break;

                    case R.drawable.middle_vertical_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.middle_vertical_wall_blue));
                        break;

                    case R.drawable.middle_straight_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.middle_straight_wall_blue));
                        break;

                    case R.drawable.left_straight_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.left_straight_wall_blue));
                        break;

                    case R.drawable.right_straight_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.right_straight_wall_blue));
                        break;

                    case R.drawable.top_left_angle_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.top_left_angle_wall_blue));
                        break;

                    case R.drawable.top_right_angle_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.top_right_angle_wall_blue));
                        break;

                    case R.drawable.top_vertical_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.top_vertical_wall_blue));
                        break;

                    case R.drawable.top_t_wall50:
                        content.append(wallRelationForLoad.get(R.drawable.top_t_wall50));
                        break;

                    case R.drawable.bottom_t_wall50:
                        content.append(wallRelationForLoad.get(R.drawable.bottom_t_wall50));
                        break;

                    case R.drawable.left_t_wall:
                        content.append(wallRelationForLoad.get(R.drawable.left_t_wall));
                        break;

                    case R.drawable.right_t_wall:
                        content.append(wallRelationForLoad.get(R.drawable.right_t_wall));
                        break;

                    case R.drawable.cross_wall:
                        content.append(wallRelationForLoad.get(R.drawable.cross_wall));
                        break;
                }

                countNumber++;
            }

            MapLine myMapLine = new MapLine( 0, i , content.toString(), id );

            MapDAO.saveMapLines( this,myMapLine );
        }
    }

    /* Called After API Response */

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
                    case R.drawable.left_player_blue :
                        content.append("P");
                        break;
                    case R.drawable.mur:
                        content.append("#");
                        break;
                    case R.drawable.blue_grass:
                        content.append(".");
                        break;
                    case R.drawable.opened_cage_blue:
                        content.append("X");
                        break;
                    case R.drawable.free_monster_blue:
                        content.append("C");
                        break;
                    case R.drawable.caged_monster_blue:
                        content.append("W");
                        break;

                    case R.drawable.bottom_left_angle_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.bottom_left_angle_wall_blue));
                        break;

                    case R.drawable.bottom_right_angle_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.bottom_right_angle_wall_blue));
                        break;

                    case R.drawable.bottom_vertical_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.bottom_vertical_wall_blue));
                        break;

                    case R.drawable.middle_vertical_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.middle_vertical_wall_blue));
                        break;

                    case R.drawable.middle_straight_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.middle_straight_wall_blue));
                        break;

                    case R.drawable.left_straight_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.left_straight_wall_blue));
                        break;

                    case R.drawable.right_straight_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.right_straight_wall_blue));
                        break;

                    case R.drawable.top_left_angle_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.top_left_angle_wall_blue));
                        break;

                    case R.drawable.top_right_angle_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.top_right_angle_wall_blue));
                        break;

                    case R.drawable.top_vertical_wall_blue:
                        content.append(wallRelationForLoad.get(R.drawable.top_vertical_wall_blue));
                        break;

                    case R.drawable.top_t_wall50:
                        content.append(wallRelationForLoad.get(R.drawable.top_t_wall50));
                        break;

                    case R.drawable.bottom_t_wall50:
                        content.append(wallRelationForLoad.get(R.drawable.bottom_t_wall50));
                        break;

                    case R.drawable.left_t_wall:
                        content.append(wallRelationForLoad.get(R.drawable.left_t_wall));
                        break;

                    case R.drawable.right_t_wall:
                        content.append(wallRelationForLoad.get(R.drawable.right_t_wall));
                        break;

                    case R.drawable.cross_wall:
                        content.append(wallRelationForLoad.get(R.drawable.cross_wall));
                        break;
                }
                countNumber++;
            }

            MapLine myMapLine = new MapLine( 0, i , content.toString(),  myMap.getIdMap());

            if ( i < nbRowTemp )
            {
                MapDAO.updateMapLines(this,myMapLine);
            }
            else
            {
                MapDAO.saveMapLines(this,myMapLine);
            }
        }
    }

     /*
      Check if there is enough finish place for the number of boxes placed.
      */
    public boolean EnoughFinishPlace ()
    {
        int nbBox = 0;
        int nbFinishZone = 0;

        for ( int j : matrix )
        {
            if ( j == images[3] )
            {
                nbFinishZone++;
            }
            else if ( j == images[4] )
            {
                nbBox++;
            }
        }
        if ( nbBox != 0 || nbFinishZone != 0 )
        {
            return ( nbBox <= nbFinishZone );
        }
        else{
            return false;
        }

    }

    /*
        Called when the user click on the map to place item.
        Call the method to refresh the board.
     */

    public void ClickOnBoard(int position)
    {
        Modification = true;
        int previousImages = matrix[position];

        if ( images[currentTool] == images[1] )
        {
            wallChecked = new ArrayList<>();
            ChooseRightWall( position ) ;
        }
        else
        {
            matrix[position] = images[currentTool];

            EmulateClickGameBoard();
        }

        if ( matrix[position] == images[0] )
        {
            nbPlayerPlaced--;
        }
        else if ( matrix[position] == images[4] )
        {
            nbBoxPlaced--;
        }

        if ( images[currentTool] == images[0] )
        {
            if (nbPlayerPlaced != 0 && positionPlayer != -1) {

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

        if ( previousImages != matrix[position])
        {
            this.runOnUiThread(() -> light.setImageResource(R.drawable.red_circle));
            myMap.setIsTested(false);
        }
        FillGameBoard();
    }

    /*
     Count Object on the Game Board
     */

    public void CountObjectOnBoard()
    {
        nbPlayerPlaced = 0;
        nbBoxPlaced = 0;

        for (int j : matrix)
        {
            if ( j == images[0] )
            {
                nbPlayerPlaced++;
            }
            else if ( j == images[4] || j == images[5])
            {
                nbBoxPlaced++;
            }

        }
    }


    /*
        Called when the player try to test his map
        Open game activity with the current map
     */
    public void TestGame()
    {
        if ( myMap.getIdMap() != 0 && !Modification){

            Intent intent=new Intent(this,GameActivity.class);
            intent.putExtra("Map", myMap);
            intent.putExtra("comingFromTest", true);
            startActivityForResult(intent, 2);
        }
        else
        {
            Toast.makeText(this, "You need to save the map first !", Toast.LENGTH_SHORT).show();
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
        Called when the user has finish to test his map or just return without finishing
        Change the light color of the test indicator depending on the result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2)
        {
            this.runOnUiThread(() ->
            {
                light.setImageResource(R.drawable.green_circle);
                myMap.setIsTested(true);
            });
        }
    }


    public void ChooseRightWall( int position )
    {
        //---------------- VARIABLES ----------------//

        int nbWallAround = 0;
        boolean up = false;
        boolean left= false;
        boolean right= false;
        boolean down= false;

        //-------------------------------------------//

        //-------------- RETRIEVE ALL 4 CASES AROUND -------------//

        try
        {
            //If it is one of our wall
            if (contains(wallsTab, matrix[ position -1 ]) )
            {
                if ( ( !leftLimits.contains(position - 1) ||  !rightLimits.contains(position ) ) && ( !rightLimits.contains(position - 1 ) && !leftLimits.contains(position) ) )
                {
                    nbWallAround++;
                    left = true;
                }

            }
        }catch(Exception e){
            System.out.println(" Error : " + e);
        }

        try
        {
            //If it is one of our wall
            if (contains(wallsTab, matrix[ position + 1 ]) )
            {
                if ( ( !rightLimits.contains(position ) || !leftLimits.contains(position + 1) ) && ( !leftLimits.contains(position + 1 ) &&  !rightLimits.contains(position ) )  )
                {
                    nbWallAround++;
                    right = true;
                }

            }
        }catch(Exception e){
            System.out.println(" Error : " + e);
        }
        try
        {
            //If it is one of our wall
            if (contains(wallsTab, matrix[ position - myMap.getNbColumns() ]) )
            {
                nbWallAround++;
                up = true;
            }
        }catch(Exception e){
            System.out.println(" Error : " + e);
        }
        try
        {
            //If it is one of our wall
            if (contains(wallsTab, matrix[ position + myMap.getNbColumns() ]) )
            {
                nbWallAround++;
                down = true;
            }

        }catch(Exception e){
            System.out.println(" Error : " + e);
        }



        //-------------------------------------------------------------//

        //--------- ACCORDING TO THE NUMBER OF SURROUNDING WALLS -----------------//
        switch ( nbWallAround )
        {

            //
            //None so we just put a basic horizontal wall
            case 0:
                //
                //Change the current wall
                matrix[position] = wallsTab[4];
                //We add the current wall to the list of walls that should not be re-visited
                wallChecked.add(position);
                break;

            // A wall around
            case 1:
                 // Depending on its position, we place the associated 'closing' wall
                 matrix[position] = ( up ? wallsTab[2] : down ? wallsTab[9] : left ? wallsTab[6] : wallsTab[5] );

                //We add the current wall to the list of walls that should not be re-visited
                wallChecked.add(position);

                  // If he is above
                  if ( up )
                  {
                      ChooseTheRightWallForUp( position );
                  }
                  //If it's below
                  else if ( down )
                  {
                      ChooseTheRightWallForDown( position );
                  }
                  //If he is on the left
                  else if ( left )
                  {
                      ChooseTheRightWallForLeft( position );
                  }
                  //If the wall is on the right
                  else {
                      ChooseTheRightWallForRight( position );
                  }
                break;

            //If there are 2 walls around
            case 2:

                //We add the current wall to the list of walls that should not be re-visited
                wallChecked.add(position);

                //If it's up and down
                if  ( up && right )
                {
                    //Change the current wall
                    matrix[position] = wallsTab[0];

                    ChooseTheRightWallForUp( position );

                    ChooseTheRightWallForRight( position );

                }
                else if ( up && left )
                {

                    //Change the current wall
                    matrix[position] = wallsTab[1];

                    ChooseTheRightWallForUp( position );

                    ChooseTheRightWallForLeft( position );

                }
                else if ( down && left )
                {

                    //Change the current wall
                    matrix[position] = wallsTab[8];
                    ChooseTheRightWallForDown( position );

                    ChooseTheRightWallForLeft( position );

                }
                else if ( down && right )
                {
                    //Change the current wall
                    matrix[position] = wallsTab[7];
                    ChooseTheRightWallForDown( position );

                    ChooseTheRightWallForRight( position );

                }
                else if (right)
                {
                    //Change the current wall
                    matrix[position] = wallsTab[4];

                    ChooseTheRightWallForLeft( position );

                    ChooseTheRightWallForRight( position );

                }
                else {
                    //Change the current wall
                    matrix[position] = wallsTab[3];

                    ChooseTheRightWallForDown( position );

                    ChooseTheRightWallForUp( position );
                }
                break;


            case 3:
                //
                //We add the current wall to the list of walls that should not be re-visited
                wallChecked.add(position);

                if ( up && right && left )
                {

                    //Change the current wall
                    matrix[position] = wallsTab[11];

                    ChooseTheRightWallForUp( position );

                    ChooseTheRightWallForRight( position );

                    ChooseTheRightWallForLeft( position );
                }
                else if (up && left)
                {

                    //Change the current wall
                    matrix[position] = wallsTab[13];

                    ChooseTheRightWallForUp( position );

                    ChooseTheRightWallForLeft( position );

                    ChooseTheRightWallForDown( position );
                }
                else if (left)
                {
                    //Change the current wall
                    matrix[position] = wallsTab[10];

                    ChooseTheRightWallForDown( position );

                    ChooseTheRightWallForRight( position );

                    ChooseTheRightWallForLeft( position );
                }
                else {
                    //Change the current wall
                    matrix[position] = wallsTab[12];

                    ChooseTheRightWallForUp( position );

                    ChooseTheRightWallForRight( position );

                    ChooseTheRightWallForDown( position );
                }
                break;

            case 4:

                //We add the current wall to the list of walls that should not be re-visited
                wallChecked.add(position);
                //Change the current wall
                matrix[position] = wallsTab[14];

                ChooseTheRightWallForDown( position );

                ChooseTheRightWallForRight( position );

                ChooseTheRightWallForUp( position );

                ChooseTheRightWallForLeft( position );
                break;
        }

    }

    public void ChooseTheRightWallForLeft( int position )
    {
        if ( !wallChecked.contains( position - 1 ) )
        {
            ChooseRightWall( position - 1);
        }
    }

    public void ChooseTheRightWallForRight( int position )
    {
        if ( !wallChecked.contains( position + 1 ) )
        {
            ChooseRightWall(position + 1);
        }
    }

    public void ChooseTheRightWallForUp( int position )
    {
        if ( !wallChecked.contains( position - myMap.getNbColumns() ) )
        {
            ChooseRightWall( position - myMap.getNbColumns() );
        }
    }

    public void ChooseTheRightWallForDown( int position )
    {
        if ( !wallChecked.contains( position + myMap.getNbColumns() ) )
        {
            ChooseRightWall( position + myMap.getNbColumns() );
        }
    }

    static public boolean contains(int[] T,int val){
        return Arrays.toString(T).contains(String.valueOf(val));
    }
    //-----------------------------

    /*
    Change the current text which is display on the pop up in parameter with the list of text
    also in parameter
 */
    public void RefreshTextPopUp(Dialog myDialog, TutoDesign popup , ArrayList<String> text )
    {
        if (popup.listenerActive) {

            if ( popup.GetCurrentText() == text.size() )
            {
                myDialog.dismiss();
                //TutorialGuide();
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
    Display pop up with text to guide the player on in first time on the game
 */
    public void TutorialGuide()
    {
        ArrayList<String> text = new ArrayList<>();
        text.add( "In this space you can create your own level! Select an element then press on the matrix.");
        text.add( "You can change the size of the level and even give it a name.");
        text.add( " You must test your level if you want it to appear in community levels.");
        text.add( " As soon as you make a change, the level will no longer be tested, you can check that with the light at the top right.");
        text.add( " You do not have to test your level if you wish, it will remain in your creation space.");
        text.add( " I hope that was clear enough. So have fun!");
        CallPopUp(text);
    }

    public void EmulateClickGameBoard()
    {
        int position = 0;
        for ( int i = 0 ; i < matrix.length; i++ )
        {
            if (  contains(wallsTab,matrix[i]))
            {
                position = i;
                break;
            }
        }
        int tempCurrentTool = currentTool;
        currentTool = 1;

        if ( position != 0 )
        {
            ClickOnBoard(position);
        }

        currentTool = tempCurrentTool;
    }

    public void FindPlayer( )
    {
        boolean playerFind = false;
        for ( int i = 0 ; i < matrix.length; i++ )
        {
            if ( matrix[i] == images[0] )
            {
                playerFind = true;
                positionPlayer = i;
            }
        }
        if ( !playerFind)
        {
            nbPlayerPlaced--;
        }
    }
}