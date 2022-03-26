<?php

class log {
	
	// -------------------------------------------
	/*
	Ecrit un log dans un fichier :
	[date et heure] [niveau critique] Texte.\n
	*/
	public static function put($text, $level = "OK", $file = "log.txt") {
		file_put_contents(
			$file, 
			"[" . date('l jS \of F Y h:i:s A') . "] [Level : " . $level . "] [Ip : " . self::getIPAddress() . "] : " . $text . "\n",
			FILE_APPEND | LOCK_EX
		);
	}
	// -------------------------------------------



	// -------------------------------------------
	public static function getIPAddress() {  
		if(!empty($_SERVER['HTTP_CLIENT_IP'])) {  
			// whether ip is from the share internet  
			$ip = $_SERVER['HTTP_CLIENT_IP'];  
		} elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {  
			// whether ip is from the proxy  
			$ip = $_SERVER['HTTP_X_FORWARDED_FOR'];  
		} elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {  
			//whether ip is from the remote address  
			$ip = $_SERVER['REMOTE_ADDR'];  
		} else {
			$ip = "LOCALHOST";
		}
		return $ip;  
	}  
	// -------------------------------------------

}