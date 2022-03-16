<?php

class MapDAO {

	// -------------------------------------------
    // Recupère Toute les maps 
    public static function GetAllMap(){
        DBConnex::runFetchAll(
            "Trying to get all maps",
            "Success retrieving all maps",
            "Failure retrieving all maps",
            "SELECT *
            FROM map
            where isTested = true"
        );
    }

    // -------------------------------------------
    // Recupère Toute les maps 
    public static function GetMapByClient(){
        DBConnex::runFetchAll(
            "Trying to get all maps of a client",
            "Success retrieving all maps of a client",
            "Failure retrieving all maps of a client",
            "SELECT *
            FROM map
            WHERE idClient = :idClient",
            array("idClient")
        );
    }

	// -------------------------------------------

    public static function SaveMap(){
        DBConnex::runQuery(
            "Trying to insert a new map",
            "Success to insert a new map",
            "Failure to insert a new map",
            "INSERT INTO map (nom,nbRows,nbColumns,idClient) VALUES ( :nameMap, :nbRows, :nbColumns, :idClient );",
            array("nameMap","nbRows","nbColumns","idClient")
        );

        DBConnex::runFetch(
            "Trying to get max id Map",
            "Success retrieving max id Map",
            "Failure retrieving max id Map",
            "SELECT max(idMap) as id
            FROM map"
        );
        
    }

    // -------------------------------------------

    public static function UpdateMap(){
        DBConnex::runQuery(
            "Trying to update a map",
            "Success to update a map",
            "Failure to update a map",
            "UPDATE map SET nom = :nameMap, nbRows = :nbRows, nbColumns = :nbColumns, idClient = :idClient, isTested =:isTested where idMap = :idMap;",
            array("nameMap","nbRows","nbColumns","idClient","idMap","isTested")
        );        
    }

    // --------------------------------------------

    public static function UpdateIsTested(){
        DBConnex::runQuery(
            "Trying to update isTested in a map",
            "Sucess to update isTested in a map",
            "Failure to update isTested in a map",
            "UPDATE map SET isTested = :tested where idMap = :idMap;",
            array("idMap", "tested")
        );  
    }

    // --------------------------------------------

    public static function DeleteMap(){
        
        DBConnex::runQuery(
            "Trying to delete all map_lines of a map",
            "Success to delete all map_lines of a map",
            "Failure to delete all map_lines of a map",
            "DELETE FROM map_ligne WHERE idMap = :idMap",
            array("idMap")
        );
        
        DBConnex::runQuery(
            "Trying to delete a map",
            "Success to delete a map",
            "Failure to delete a map",
            "DELETE FROM map WHERE idMap = :idMap",
            array("idMap")
        );
    }


    
}
