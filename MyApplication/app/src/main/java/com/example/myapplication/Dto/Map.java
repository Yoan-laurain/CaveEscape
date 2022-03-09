package com.example.myapplication.Dto;

import android.app.Activity;

import com.example.myapplication.Game.GameActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class Map implements Serializable
{
    private final int idMap;
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

    //------------------------------------------------------------------------------

    public int getIdMap() { return idMap; }

    public String getNom() { return nom; }

    public int getNbRows() { return nbRows; }

    public int getNbColumns() { return nbColumns; }

    public String getIdClient() { return idClient; }

    public boolean getIsTested() { return isTested; }

    //------------------------------------------------------------------------------

    public static Map hydrateMap(JSONObject json) throws JSONException
    {
        return new Map(
                json.getInt("idMap"),
                json.getString("nom"),
                json.getInt("nbRows"),
                json.getInt("nbColumns"),
                ( json.getInt("isTested") == 1 ? true : false),
                json.getString("idClient")
        );
    }

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

            JSONObject json = new JSONObject(jsonMapString);

            myMap = Map.hydrateMap( json );

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return myMap;

    }

    public static void HardCodedMap(GameActivity myActivity) {

        String jsonString = "[\n" +
                "    {\n" +
                "        \"id\":\"2\",\n" +
                "        \"indexRow\":\"0\",\n" +
                "        \"content\":\"######\",\n" +
                "        \"idMap\":\"-1\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"3\",\n" +
                "        \"indexRow\":\"1\",\n" +
                "        \"content\":\"#..P.#\",\n" +
                "        \"idMap\":\"-1\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"4\",\n" +
                "        \"indexRow\":\"2\",\n" +
                "        \"content\":\"#C...#\",\n" +
                "        \"idMap\":\"-1\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"5\",\n" +
                "        \"indexRow\":\"3\",\n" +
                "        \"content\":\"#.#..#\",\n" +
                "        \"idMap\":\"-1\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"6\",\n" +
                "        \"indexRow\":\"4\",\n" +
                "        \"content\":\"#X#..#\",\n" +
                "        \"idMap\":\"-1\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\":\"7\",\n" +
                "        \"indexRow\":\"5\",\n" +
                "        \"content\":\"######\",\n" +
                "        \"idMap\":\"-1\"\n" +
                "    }\n" +
                "]";
        try {
            JSONArray jsonArrayMap = new JSONArray(jsonString);
            HashMap<Integer, MapLine> LinesMaps = new HashMap<>();

            for (int i = 0; i < jsonArrayMap.length(); i++) {
                JSONObject json = jsonArrayMap.getJSONObject(i);

                MapLine myMapLine = MapLine.hydrateMap(json);

                LinesMaps.put(myMapLine.getId(), myMapLine);
            }

            myActivity.responseMapLine(LinesMaps);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
