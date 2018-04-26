<?php
session_start();
include '../config/checkSession.php';

if(Check::checkUser())
{
	session_destroy();
	header("location: login.php?msg=Успешно се одјавивте");
}
else
{
	header("location: login.php?msg=Мора да сте најавени за да се одјавите");
}