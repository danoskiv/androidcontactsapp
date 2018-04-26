<?php
session_start();

$pdo = require '../config/bootstrap.php';
include '../classes/Awards.php';

$award = new Awards($pdo);

if(isset($_POST["id"]) && isset($_POST["name"]) && isset($_POST["value"]) && isset($_POST["category"]))
{
	$id = intval($_POST["id"]);
	$name = $_POST["name"];
	$value = intval($_POST["value"]);
	$pid = intval($_POST["category"]);

	if($award->updateAward($id, $name, $value, $pid))
	{
		$msg = "SUCCESS!";
		header('Content-Type: application/json');
		print(json_encode(array("message" => $msg)));
	}
	else {
		$msg = "FAIL!";
		header('Content-Type: application/json');
		print(json_encode(array("message" => $msg)));
	}
}
