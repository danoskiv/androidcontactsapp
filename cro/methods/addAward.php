<?php
session_start();
$pdo = require '../config/bootstrap.php';
include '../config/checkSession.php';
include '../classes/Questions.php';

if(Check::checkAdmin())
{
	$question = new Questions($pdo);
	$result = $question->getAllParents();
?>

<!DOCTYPE html>
<html>
<head>
	<link rel=stylesheet href="../styles/style.css">
	<title>Нова награда</title>
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
	<form method="POST" action="postAddAward.php">
		<div class="container">
			<label for="kategorija">Одберете категорија:</label>
			<select name="option">
				<?php 
					if($result)
					{
						for($i = 0; $i < count($result); $i++)
						{
				?>
							<option value="<?php echo $result[$i]['pid'];?>"><?php echo $result[$i]['body'];?></option>
				<?php
						}
					}
				?>
			<label for="name"><b>Награда:</b></label>
			<input type="text" name="name" placeholder="Внесете го називот на наградата" required>

			<label for="value"><b>Број на награди:</b></label><br>

			<center><input type="number" name="amountInput" min="1" max="100" value="0" oninput="this.form.amountRange.value=this.value" required></center><br>
			<input type="range" name="amountRange" min="1" max="100" value="0" oninput="this.form.amountInput.value=this.value" required>

			<button type="submit">Додади награда</button>
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