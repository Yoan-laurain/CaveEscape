<?php

class MapLigneDAO {

	// -------------------------------------------
    public static function getMapLigneById(){

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

    public static function saveMapLine(){

        DBConnex::runQuery(
            "Trying to insert a map_line",
            "Sucess insert a map_line",
            "Failure insert a map_line",
            "INSERT INTO map_ligne (indexRow,content,idMap) VALUES ( :indexRow, :content, :idMap );",
            array("indexRow","content","idMap")
        );

    }

    
    
}
