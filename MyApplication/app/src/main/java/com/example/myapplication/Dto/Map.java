package com.example.myapplication.Dto;

import org.json.JSONException;
import org.json.JSONObject;

public class Map
{
    private final int idMap;
    private final String nom;
    private final int nbRows;
    private final int nbColumns;
    private final int idClient;

    public Map(int idMap, String nom, int nbRows, int nbColumns, int idClient)
    {
        this.idMap = idMap;
        this.nom = nom;
        this.nbRows = nbRows;
        this.nbColumns = nbColumns;
        this.idClient = idClient;
    }

    public int getIdMap() { return idMap; }

    public String getNom() { return nom; }

    public int getNbRows() { return nbRows; }

    public int getNbColumns() { return nbColumns; }

    public int getIdClient() { return idClient; }

    public static Map hydrateMap(JSONObject json) throws JSONException
    {
        Map myMap = new Map(
                json.getInt("idMap"),
                json.getString("nom"),
                json.getInt("nbRows"),
                json.getInt("nbColumns"),
                json.getInt("idClient")
        );
        return myMap;
    }
}
