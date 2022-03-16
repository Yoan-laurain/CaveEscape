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
        $messageStart,
        $messageSucess,
        $messageFail,
        $sql,
        $params = null
    ) {
        DBConnex::run(
            $messageStart,
            $messageSucess,
            $messageFail,
            $sql,
            $params,
            True
        );
    }

    public static function runFetch(
        $messageStart,
        $messageSucess,
        $messageFail,
        $sql,
        $params = null
    ) {
        DBConnex::run(
            $messageStart,
            $messageSucess,
            $messageFail,
            $sql,
            $params
        );
    }

    public static function runFetchAll(
        $messageStart,
        $messageSucess,
        $messageFail,
        $sql,
        $params = null
    ) {
        DBConnex::run(
            $messageStart,
            $messageSucess,
            $messageFail,
            $sql,
            $params,
            False,
            True
        );
    }
	// -------------------------------------------



	// -------------------------------------------
    public static function run(
        $messageStart,
        $messageSucess,
        $messageFail,
        $sql,
        $params = null,
        $isQuery = False,
        $fetchAll = False
        ) {
        try{
            log::put($messageStart);

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
                log::put($messageSucess);
                json::return($reponse);
            } 
            else {
                log::put($messageFail, "WARNING");
            }
        

        }catch(Exception $e){
            log::put($messageFail . '\n' . $e->getMessage() ,"ERROR");
        }
    }
	// -------------------------------------------
    


	// -------------------------------------------
    function __construct() {
        try {
            parent::__construct(Param::$dsn ,Param::$user, Param::$pass);
        } catch (Exception $e) {   
            log::put("Impossible de se connecter.\n" . $e->getMessage(), "ERROR");       
            die();
        }
    }
	// -------------------------------------------

}
