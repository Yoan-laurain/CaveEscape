package com.example.myapplication.Dto;

import org.json.JSONException;
import org.json.JSONObject;

public class MapLine
{
    private int id;
    private int indexRow;
    private String content;
    private int idMap;

    public MapLine(int id, int indexRow, String content, int idMap)
    {
        this.id = id;
        this.indexRow = indexRow;
        this.content = content;
        this.idMap = idMap;
    }

    //-------------------------------------------------------------------------

    public void setIndexRow(int indexRow) { this.indexRow = indexRow; }

    public void setContent(String content) { this.content = content; }

    public void setIdMap(int idMap) { this.idMap = idMap; }

    //-------------------------------------------------------------------------

    public int getId() { return id; }

    public int getIndexRow() { return indexRow; }

    public String getContent() { return content; }

    public int getIdMap() { return idMap; }

    //-------------------------------------------------------------------------

    public static MapLine hydrateMap(JSONObject json) throws JSONException
    {
        MapLine myMap = new MapLine(
                json.getInt("id"),
                json.getInt("indexRow"),
                json.getString("content"),
                json.getInt("idMap")
        );
        return myMap;
    }
}
