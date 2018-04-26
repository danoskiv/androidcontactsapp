<?php

$pdo = require '../config/bootstrap.php';
include '../classes/Contacts.php';

$contact = new Contacts($pdo);
if(isset($_POST["id"]) && isset($_POST["name"]) && isset($_POST["email"]) && isset($_POST["phone"]) && isset($_POST["user_id"]))
{
	$id = $_POST["id"];

	$name = $_POST["name"];

	$email = $_POST["email"];

	$phone = $_POST["phone"];

	$user_id = $_POST["user_id"];


	$msg = "SUCCESS!";
	$msg2 = "FAIL!";

	if($contact->addContact($id, $name, $email, $phone, $user_id))
	{
		header('Content-Type: application/json');
		print(json_encode(array("message" => $msg)));}
	else
	{
		header('Content-Type: application/json');
		print(json_encode(array("message" => $msg2)));
	}
}
else
{
	$msg2 = "FAIL!";
	header('Content-Type: application/json');
	print(json_encode(array("message" => $msg2)));
}
