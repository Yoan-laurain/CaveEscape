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
    
}
