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
    private String idClient;

    //------------------------------------------------------------------------------

    public Map(int idMap, String nom, int nbRows, int nbColumns, String idClient)
    {
        this.idMap = idMap;
        this.nom = nom;
        this.nbRows = nbRows;
        this.nbColumns = nbColumns;
        this.idClient = idClient;
    }

    //------------------------------------------------------------------------------

    public void setIdClient ( String idClient ) { this.idClient = idClient; }

    public void setNbRows ( int nbRows ) { this.nbRows = nbRows;}

    public void setNbColumns ( int nbColumns ) { this.nbColumns = nbColumns;}

    public void setName ( String name ) { this.nom = name;}

    //------------------------------------------------------------------------------

    public int getIdMap() { return idMap; }

    public String getNom() { return nom; }

    public int getNbRows() { return nbRows; }

    public int getNbColumns() { return nbColumns; }

    public String getIdClient() { return idClient; }

    //------------------------------------------------------------------------------

    public static Map hydrateMap(JSONObject json) throws JSONException
    {
        Map myMap = new Map(
                json.getInt("idMap"),
                json.getString("nom"),
                json.getInt("nbRows"),
                json.getInt("nbColumns"),
                json.getString("idClient")
        );
        return myMap;
    }
}
