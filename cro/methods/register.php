<?php
session_start();
if(isset($_SESSION['logged_in']) && isset($_SESSION['user']))
{
	echo "VEKE STE REGISTRIRANI, ODJAVETE SE ZA DA PRODOLZITE KON REGISTRACIJA";
	header("Refresh: 3, home.php");
}
else{
?>
<!DOCTYPE html>
<html>
<head>
	<title>Најава</title>
	<link rel=stylesheet href="../styles/style.css">
</head>
<body>
	<form method="POST" action="postRegister.php">
		<div class="container">
			<label for="name"><b>Име и презиме:</b></label>
			<input type="text" name="name" placeholder="Вашето име и презиме" required>

			<label for="uname"><b>Корисничко име:</b></label>
			<input type="text" placeholder="Внесете го корисничкото име" name="uname" required>

			<label for="psw"><b>Лозинка:</b></label>
			<input type="password" placeholder="Внесете ја лозинката" name="psw" required>

			<button type="submit">Регистрирај се</button>
		</div>
	</form>
	<div class="container">
		<input type="button" onclick="location.href='login.php'" value="Најави се!">
	</div>
	
</body>
</html>
<?php
};