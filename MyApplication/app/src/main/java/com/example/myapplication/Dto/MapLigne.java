package com.example.myapplication.Dto;

import org.json.JSONException;
import org.json.JSONObject;

public class MapLigne
{

    private int id;
    private int indexRow;
    private String content;
    private int idMap;

    public MapLigne(int id, int indexRow, String content, int idMap)
    {
        this.id = id;
        this.indexRow = indexRow;
        this.content = content;
        this.idMap = idMap;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getIndexRow() { return indexRow; }

    public void setIndexRow(int indexRow) { this.indexRow = indexRow; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public int getIdMap() { return idMap; }

    public void setIdMap(int idMap) { this.idMap = idMap; }

    public static MapLigne hydrateMap(JSONObject json) throws JSONException
    {
        MapLigne myMap = new MapLigne(
                json.getInt("id"),
                json.getInt("indexRow"),
                json.getString("content"),
                json.getInt("idMap")
        );
        return myMap;
    }
}
