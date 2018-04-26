<?php

class Connect
{
	public static function make(){
		require_once 'Config.php';
		try {
			
			$conn = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME . ";charset=utf8", DB_USERNAME, DB_PASSWORD);
		    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			return $conn;

		} catch (PDOException $e) {
			return $e->getMessage();
		}
	}
}