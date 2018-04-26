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
	header("location: addUser.php?msg=success");
}
else
{
	header("location: addUser.php?msg=fail");
}