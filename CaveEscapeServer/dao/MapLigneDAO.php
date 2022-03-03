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
    
}
