package com.example.myapplication.Dto;

import android.content.res.Resources;
import com.example.myapplication.Game.GameActivity;
import com.example.myapplication.LevelSelect.SelectActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

public class Map implements Serializable
{
    private int idMap;
    private String nom;
    private int nbRows;
    private  int nbColumns;
    private final String idClient;
    private boolean isTested;

    //------------------------------------------------------------------------------

    public Map(int idMap, String nom, int nbRows, int nbColumns, boolean isTested,String idClient)
    {
        this.idMap = idMap;
        this.nom = nom;
        this.nbRows = nbRows;
        this.nbColumns = nbColumns;
        this.idClient = idClient;
        this.isTested = isTested;
    }

    //------------------------------------------------------------------------------

    public void setNbRows ( int nbRows ) { this.nbRows = nbRows;}

    public void setNbColumns ( int nbColumns ) { this.nbColumns = nbColumns;}

    public void setName ( String name ) { this.nom = name;}

    public void setIsTested ( boolean isTested ) { this.isTested = isTested;}

    public void setIdMap( int idMap ) { this.idMap = idMap;}

    //------------------------------------------------------------------------------

    public int getIdMap() { return idMap; }

    public String getNom() { return nom; }

    public int getNbRows() { return nbRows; }

    public int getNbColumns() { return nbColumns; }

    public String getIdClient() { return idClient; }

    public boolean getIsTested() { return isTested; }

    //------------------------------------------------------------------------------

    /*
        Hydrate a map object with value of the json object in parameter
     */
    public static Map HydrateMap( JSONObject json ) throws JSONException
    {
        return new Map(
                json.getInt("idMap"),
                json.getString("nom"),
                json.getInt("nbRows"),
                json.getInt("nbColumns"),
                (json.getInt("isTested") == 1),
                json.getString("idClient")
        );
    }

    /*
        Hydrate a map object with a created json
     */
    public static Map HardCodedMapHeader()
    {
        Map myMap = new Map(0,"", 0, 0,false,"0");

        String jsonMapString = "{\n" +
                "        \"idMap\":\"-1\",\n" +
                "        \"nom\":\"HardCoded Map\",\n" +
                "        \"nbRows\":\"6\",\n" +
                "        \"nbColumns\":\"6\",\n" +
                "        \"idClient\":\"1\",\n" +
                "        \"isTested\":\"1\"\n" +
                "    }";

        try
        {
            JSONObject json = new JSONObject( jsonMapString );
            myMap = Map.HydrateMap( json );
        }
        catch (JSONException ignored){}

        return myMap;
    }

    /*
        Hydrate map line object with a created json and called method
        in GameActivity to load map
     */
    public static void HardCodedMap( GameActivity myActivity )
    {

        String jsonString = "[\n" +
                "    {\n" +
                "        \"id\":\"2\",\n" +
                "        \"indexRow\":\"0\",\n" +
                "        \"content\":\"HEEEEI\",\n" +
                "        \"idMap\":\"-1\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"3\",\n" +
                "        \"indexRow\":\"1\",\n" +
                "        \"content\":\"D..P.D\",\n" +
                "        \"idMap\":\"-1\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"4\",\n" +
                "        \"indexRow\":\"2\",\n" +
                "        \"content\":\"DC...D\",\n" +
                "        \"idMap\":\"-1\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"5\",\n" +
                "        \"indexRow\":\"3\",\n" +
                "        \"content\":\"D.D..D\",\n" +
                "        \"idMap\":\"-1\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"6\",\n" +
                "        \"indexRow\":\"4\",\n" +
                "        \"content\":\"DXD..D\",\n" +
                "        \"idMap\":\"-1\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"7\",\n" +
                "        \"indexRow\":\"5\",\n" +
                "        \"content\":\"AEUEEB\",\n" +
                "        \"idMap\":\"-1\"\n" +
                "    }\n" +
                "]";
        try
        {
            JSONArray jsonArrayMap = new JSONArray( jsonString );
            HashMap<Integer, MapLine> LinesMaps = new HashMap<>();

            for (int i = 0; i < jsonArrayMap.length(); i++)
            {
                JSONObject json = jsonArrayMap.getJSONObject( i );

                MapLine myMapLine = MapLine.hydrateMap( json );

                LinesMaps.put( myMapLine.getId(), myMapLine );
            }

            myActivity.ResponseMapLine( LinesMaps );

        } catch ( JSONException ignored ) {}

    }

    /*
        Hydrate a map object with json in a file
     */

    public static Map FileMapHeader( SelectActivity myActivity )
    {
        Map myMap = new Map(0,"", 0, 0,false,"0");

        String json;
        int id = myActivity.getResources().getIdentifier("mapheader", "raw", myActivity.getPackageName());

        try
        {
            Resources r = myActivity.getResources();
            InputStream is = r.openRawResource(id);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = is.read();

            while (i != -1)
            {
                baos.write(i);
                i = is.read();
            }

            json = baos.toString();
            is.close();

            JSONObject jsonHeader = new JSONObject(json);
            myMap = Map.HydrateMap(jsonHeader);

        } catch (JSONException | IOException ignored) {}

        return myMap;
    }

    /*
        Hydrate a map line object with json in a file
     */
    public static void FileMapLine( GameActivity myActivity )
    {
        String jsonLines;
        int id = myActivity.getResources().getIdentifier("mapline", "raw", myActivity.getPackageName());

        try
        {
            Resources r = myActivity.getResources();
            InputStream is = r.openRawResource(id);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = is.read();

            while (i != -1)
            {
                baos.write(i);
                i = is.read();
            }

            jsonLines = baos.toString();
            is.close();

            JSONArray jsonArrayMap = new JSONArray(jsonLines);
            HashMap<Integer, MapLine> LinesMaps = new HashMap<>();

            for (int j = 0; j < jsonArrayMap.length(); j++) {

                JSONObject json = jsonArrayMap.getJSONObject(j);

                MapLine myMapLine = MapLine.hydrateMap(json);

                LinesMaps.put(myMapLine.getId(), myMapLine);
            }

            myActivity.ResponseMapLine(LinesMaps);

        } catch (IOException | JSONException ignored) {}
    }
}
