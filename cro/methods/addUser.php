<?php
session_start();
include '../config/checkSession.php';

if(Check::checkAdmin())
{
?>

<!DOCTYPE html>
<html>
<head>
	<link rel=stylesheet href="../styles/style.css">
	<title>Нов корисник</title>
</head>
<body>
	<?php require '../views/partialHeader.php'; ?>
	<center style="padding: 20px;" id="hideMe">
		<?php 
	 		if(isset($_GET['msg']) && $_GET['msg'] == 'success')
	 		{
	 			echo "Внесувањето беше успешно.";
	 		} 
	 		else if(isset($_GET['msg']) && $_GET['msg'] == 'fail')
	 		{
	 			echo "Внесувањето беше неуспешно. Обидете се повторно.";
	 		}
	 	?>	
	 </center>
	<form method="POST" action="postAddUser.php">
		<div class="container">
			<label for="name"><b>Име и презиме:</b></label>
			<input type="text" name="name" placeholder="Внесете име и презиме" required>

			<label for="uname"><b>Корисничко име:</b></label>
			<input type="text" placeholder="Внесете го корисничкото име" name="uname" required>

			<label for="psw"><b>Лозинка:</b></label>
			<input type="password" placeholder="Внесете ја лозинката" name="psw" required>

			<button type="submit">Додај корисник</button>
		</div>
	</form>
</body>
</html>
<?php
}
else
{
	echo "Nemate prava da dodavate korisnici";
	header("Refresh: 2, home.php");
}