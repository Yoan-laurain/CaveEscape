package com.CaveEscape.myapplication.Dto;

import org.json.JSONException;
import org.json.JSONObject;

public class MapLine
{
    //------------------------------------------------------------------------------

    private final int id;
    private final int indexRow;
    private String content;
    private final int idMap;

    //-------------------------------------------------------------------------

    public MapLine(int id, int indexRow, String content, int idMap)
    {
        this.id = id;
        this.indexRow = indexRow;
        this.content = content;
        this.idMap = idMap;
    }

    //-------------------------------------------------------------------------

    public void setContent(String content) { this.content = content; }

    //-------------------------------------------------------------------------

    public int getId() { return id; }

    public int getIndexRow() { return indexRow; }

    public String getContent() { return content; }

    public int getIdMap() { return idMap; }

    //-------------------------------------------------------------------------

    public static MapLine hydrateMap(JSONObject json) throws JSONException
    {
        return new MapLine(
                json.getInt("id"),
                json.getInt("indexRow"),
                json.getString("content"),
                json.getInt("idMap")
        );
    }
}
