<?php

class MapDAO {

	// -------------------------------------------
    // Recupère Toute les maps 
    public static function getAllMap(){
        DBConnex::runFetchAll(
            "SELECT *
            FROM map"
        );
    }

    // -------------------------------------------
    // Recupère Toute les maps 
    public static function getMapByClient(){
        DBConnex::runFetchAll(
            "SELECT *
            FROM map
            WHERE idClient"
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

    // --------------------------------------------

    public static function deleteMap(){
        DBConnex::runQuery(
            "DELETE FROM 'map' WHERE idMap = :idMap",
            array("idMap")
        );
    }
    
}
