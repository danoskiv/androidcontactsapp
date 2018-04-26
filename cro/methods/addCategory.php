<?php
session_start();
include '../classes/Questions.php';
$pdo = require '../config/bootstrap.php';

$kategorija = $_POST['kategorija'];
$user_id = $_SESSION['id'];

$category = new Questions($pdo);

if($category->addParent($kategorija, $user_id))
{
	header("location: addQuestion.php?msg=success");
}
else
{
	header("location: addQuestion.php?msg=fail");
}
