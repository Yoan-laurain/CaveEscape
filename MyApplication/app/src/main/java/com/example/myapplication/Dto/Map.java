package com.example.myapplication.Dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Map implements Serializable
{
    private final int idMap;
    private String nom;
    private int nbRows;
    private  int nbColumns;
    private final String idClient;
    private final boolean isTested;

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
}
