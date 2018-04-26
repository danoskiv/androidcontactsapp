<?php
session_start();
include '../classes/Users.php';
include '../config/checkSession.php';

$pdo = require '../config/bootstrap.php';

$user = new Users($pdo);

if(Check::checkUser())
{
	$name = $user->getNameByUsername($_SESSION['user']);
?>

<!DOCTYPE html>
<html>
<head>
	<link rel=stylesheet href="../styles/style.css">
	<title>Почетна</title>
</head>
<body>
	<?php require '../views/partialHeader.php'; ?>
</body>
</html>

<?php
}
else
{
	header("location: login.php");
}