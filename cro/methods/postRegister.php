<?php
session_start();

include '../classes/Users.php';
$pdo = require '../config/bootstrap.php';

$name = $_POST['name'];
$username = $_POST['uname'];
$password = $_POST['psw'];

$user = new Users($pdo);

if($user->addNewUser($name, $username, $password))
{
	$_SESSION['logged_in'] = true;
	$result = $user->getUserByUsername($username);
	$_SESSION['name'] = $result['name'];
	$_SESSION['id'] = $result['id'];
	$_SESSION['user'] = $username;
	header("location: home.php");
}
else
{
	header("location: register.php");
}