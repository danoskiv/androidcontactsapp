<?php
session_start();

$pdo = require '../config/bootstrap.php';
include '../classes/Awards.php';
include '../classes/Questions.php';

$awards = new Awards($pdo);
$question = new Questions($pdo);

$result = $awards->getAllAwards();


?>

<!DOCTYPE html>
<html>
<head>
	<link rel=stylesheet href="../styles/style.css">
	<title>Награди</title>
</head>
<body>
	<?php require 'partialHeader.php'; ?>
	<div id="divTable">
		<?php 
		if(count($result) > 0)
		{
		?>
		<table id="table">
			<tr>
				<th>Назив на наградата</th>
				<th>Бројна вредност</th>
				<th>Категорија</th>
			</tr>
			<?php
				for($i = 0; $i < count($result); $i++)
				{
			?>
			<tr>
				<td>
					<?php echo $result[$i]["name"]; ?>
				</td>
				<td>
					<?php echo $result[$i]["value"]; ?>
				</td>
				<td>
					<?php echo $question->getCategoryById($result[$i]["pid"]); ?>
				</td>
			</tr>
			<?php
				}
			}
			?>
		</table>
		<?php
		if(count($result) == 0)
			{
				echo "<h1 style='width:100%; text-align:center;'>Во моментов нема внесено награди.</h1>";
			}
			?>
	</div>
</body>
</html>