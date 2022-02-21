<?php
require_once 'dao/param.php';
require_once 'dao/dBConnex.php';
require_once 'lib/json.php';
require_once 'dao/MapDAO.php';
require_once 'dao/MapLigneDAO.php';


/*
$_POST['command'] = "getAllMap";
$_POST['idMap'] = 1;
*/

// -------------------------------------------
// Redirige vers la bonne commande
if (isset($_POST['command'])) {
	// Verifie si la commande existe
	$files = scandir("dao");
	$max = count($files);
	$count = 0;
	$found = false;
	$class = "";
	while (!$found && $count < $max) {
		$dao = str_replace(".php", "", $files[$count]);
		if (method_exists($dao, $_POST['command'])) {
			$found = true;
			$class = $dao;
		} else {
			$count++;
		}
	}
	if ($found) {
		call_user_func($dao . "::" . $_POST['command']);
	} 
} 
// -------------------------------------------
