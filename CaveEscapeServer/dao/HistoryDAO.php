<?php

class HistoryDAO {

	// -------------------------------------------
     
    public static function InsertNewPlayer(){
        DBConnex::runQuery(
            "Trying to insert new player",
            "Sucess  insert new player",
            "Failure  insert new player",
            " INSERT INTO history (idClient,dateInstall) VALUES (:idClient,'". date("Y-m-d H:i:s") . "')",
            array("idClient")
        );
    }
}