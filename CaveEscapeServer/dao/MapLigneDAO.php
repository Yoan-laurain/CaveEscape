<?php

class MapLigneDAO {

	// -------------------------------------------
    public static function getMapLigneById(){

        DBConnex::runFetchAll(
            "SELECT *
            FROM map_ligne
            WHERE idMap = :idMap",
            array("idMap")
        );
    }
	// -------------------------------------------

    public static function saveMapLine(){

        DBConnex::runQuery(
            "INSERT INTO map_ligne (indexRow,content,idMap) VALUES ( :indexRow, :content, :idMap );",
            array("indexRow","content","idMap")
        );

    }

    
    
}
