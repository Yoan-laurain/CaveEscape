<?php

class MapLigneDAO {

	// -------------------------------------------
    public static function GetMapLigneById(){

        DBConnex::runFetchAll(
            "Trying to get all map_line of a map",
            "Sucess retrieving all map_line of a map",
            "Failure retrieving all map_line of a map",
            "SELECT *
            FROM map_ligne
            WHERE idMap = :idMap
            ORDER by indexRow",
            array("idMap")
        );
    }
	// -------------------------------------------

    public static function SaveMapLine(){

        DBConnex::runQuery(
            "Trying to insert a new map_line",
            "Sucess insert a new  map_line",
            "Failure insert a new map_line",
            "INSERT INTO map_ligne (indexRow,content,idMap) VALUES ( :indexRow, :content, :idMap );",
            array("indexRow","content","idMap")
        );

    }
    // -------------------------------------------

    public static function UpdateMapLine(){

        DBConnex::runQuery(
            "Trying to update a map_line",
            "Sucess update a map_line",
            "Failure update a map_line",
            "UPDATE map_ligne SET content = :content where idMap = :idMap AND indexRow = :indexRow;",
            array("indexRow","content","idMap")
        );

    }

    
    
}
