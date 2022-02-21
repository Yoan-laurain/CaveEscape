<?php

require_once "param.php";


class DBConnex extends PDO{
    
	// -------------------------------------------
    private static $instance;
	// -------------------------------------------
    

    
	// -------------------------------------------
    public static function getInstance() {
        if (!self::$instance){
            self::$instance = new DBConnex();
        }
        return self::$instance;
    }
	// -------------------------------------------



	// -------------------------------------------
    public static function runQuery(
        $sql,
        $params = null
    ) {
        DBConnex::run(
            $sql,
            $params,
            True
        );
    }

    public static function runFetch(
        $sql,
        $params = null
    ) {
        DBConnex::run(
            $sql,
            $params
        );
    }

    public static function runFetchAll(
        $sql,
        $params = null
    ) {
        DBConnex::run(
            $sql,
            $params,
            False,
            True
        );
    }
	// -------------------------------------------



	// -------------------------------------------
    public static function run(
        $sql,
        $params = null,
        $isQuery = False,
        $fetchAll = False
        ) {
        try{
            $requetePrepa = DBConnex::getInstance()->prepare($sql);
            if ($params != null) {
                foreach ($params as $param) {
                    $requetePrepa->bindParam(":" . $param, $_POST[$param]);
                }
            }
            $reponse = $requetePrepa->execute();
            
            if (!$isQuery) {
                if (!$fetchAll) {
                    $reponse = $requetePrepa->fetch(PDO::FETCH_ASSOC);
                } else {
                    $reponse = $requetePrepa->fetchAll(PDO::FETCH_ASSOC);
                }
            }

            if (!empty($reponse) && $reponse != false) {
               json::return($reponse);
            } 
            //WIP à gerer si ça plante

        }catch(Exception $e){
            //WIP : si ça plante gérer le catch
        }
    }
	// -------------------------------------------
    


	// -------------------------------------------
    function __construct() {
        try {
            parent::__construct(Param::$dsn ,Param::$user, Param::$pass);
        } catch (Exception $e) {          
            die();
        }
    }
	// -------------------------------------------

}
