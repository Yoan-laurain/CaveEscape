<?php

class MapDAO {

	// -------------------------------------------
    // Recupère Toute les maps 
    public static function getAllMap(){
        DBConnex::runFetchAll(
            "Trying to get all maps",
            "Sucess retrieving all maps",
            "Failure retrieving all maps",
            "SELECT *
            FROM map"
        );
    }

    // -------------------------------------------
    // Recupère Toute les maps 
    public static function getMapByClient(){
        DBConnex::runFetchAll(
            "Trying to get all maps of a client",
            "Sucess retrieving all maps of a client",
            "Failure retrieving all maps of a client",
            "SELECT *
            FROM map
            WHERE idClient = :idClient",
            array("idClient")
        );
    }

	// -------------------------------------------

    public static function saveMap(){
        DBConnex::runQuery(
            "Trying to insert a new map",
            "Sucess to insert a new map",
            "Failure to insert a new map",
            "INSERT INTO map (nom,nbRows,nbColumns,idClient) VALUES ( :nameMap, :nbRows, :nbColumns, :idClient );",
            array("nameMap","nbRows","nbColumns","idClient")
        );

        DBConnex::runFetch(
            "Trying to get max id Map",
            "Sucess retrieving max id Map",
            "Failure retrieving max id Map",
            "SELECT max(idMap) as id
            FROM map"
        );
        
    }

    // -------------------------------------------

    public static function updateMap(){
        DBConnex::runQuery(
            "Trying to update a map",
            "Sucess to update a map",
            "Failure to update a map",
            "UPDATE map SET nom = :nameMap, nbRows = :nbRows, nbColumns = :nbColumns, idClient = :idClient where idMap = :idMap;",
            array("nameMap","nbRows","nbColumns","idClient","idMap")
        );        
    }

    // --------------------------------------------

    public static function deleteMap(){
        
        DBConnex::runQuery(
            "Trying to delete all map_lines of a map",
            "Sucess to delete all map_lines of a map",
            "Failure to delete all map_lines of a map",
            "DELETE FROM map_ligne WHERE idMap = :idMap",
            array("idMap")
        );
        
        DBConnex::runQuery(
            "Trying to delete a map",
            "Sucess to delete a map",
            "Failure to delete a map",
            "DELETE FROM map WHERE idMap = :idMap",
            array("idMap")
        );
    }
    
}
