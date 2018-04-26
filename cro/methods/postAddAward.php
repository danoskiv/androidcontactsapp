<?php
session_start();
include '../classes/Awards.php';
$pdo = require '../config/bootstrap.php';

$name = $_POST['name'];
$number = $_POST['amountInput'];
$pid = $_POST['option'];

$award = new Awards($pdo);

if($award->addAward($name, $number, $pid))
{
	header("location: addAward.php?msg=success");
}
else
{
	header("location: addAward.php?msg=fail");
}