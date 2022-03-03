<?php

class MapDAO {

	// -------------------------------------------
    // Recupère la derniere vente
    public static function getAllMap(){
        DBConnex::runFetchAll(
            "SELECT *
            FROM map"
        );
    }

	// -------------------------------------------

    public static function saveMap(){
        DBConnex::runQuery(
            "INSERT INTO map (nom,nbRows,nbColumns,idClient) VALUES ( :nameMap, :nbRows, :nbColumns, :idClient );",
            array("nameMap","nbRows","nbColumns","idClient")
        );

        DBConnex::runFetch(
            "SELECT max(idMap) as id
            FROM map"
        );
        
    }

    public static function saveMapLine(){

        DBConnex::runQuery(
            "INSERT INTO map_ligne (indexRow,content,idMap) VALUES ( :indexRow, :content, :idMap );",
            array("indexRow","content","idMap")
        );

    }
    
}
