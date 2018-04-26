<?php
session_start();
if(isset($_SESSION['logged_in']) && isset($_SESSION['user']))
{
	echo "VEKE STE NAJAVENI, ODJAVETE SE ZA DA PRODOLZITE KON REGISTRACIJA";
	header("Refresh: 2, home.php");
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

	<center id="hideMe">
		<?php 
			if(isset($_GET['msg']))
				echo $_GET['msg']; 
		?>
	</center>
	<form method="POST" action="postLogin.php">
		<div class="container">
			<label for="uname"><b>Корисничко име:</b></label>
			<input type="text" placeholder="Внесете го корисничкото име" name="uname" required>

			<label for="psw"><b>Лозинка:</b></label>
			<input type="password" placeholder="Внесете ја лозинката" name="psw" required>

			<button type="submit">Најави се!</button>
		</div>
	</form>
	<script type="text/javascript">
		var x = document.getElementById('hideMe');
		x.addEventListener("webkitAnimationEnd", myEndFunction);

		function myEndFunction()
		{
			this.style.height = 0;
			this.style.width = 0;
		}
	</script>
	
</body>
</html>
<?php
};
?> 