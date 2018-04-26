<?php
session_start();

$pdo = require '../config/bootstrap.php';
include '../classes/Users.php';
include '../classes/Questions.php';

$user = new Users($pdo);
$questons = new Questions($pdo);

$result = $questons->getAllQuestions(); //site prasanja koi imaat kategorija, bez odgovori

$kategorii = $questons->getAllCategories(); //site kategorii

?>

<!DOCTYPE html>
<html>
<head>
	<link rel=stylesheet href="../styles/style.css">
	<title>Контакти</title>
</head>
<body>
	<?php require 'partialHeader.php'; ?>
	<div id="divTableUsers">
		<?php 
		if(count($result) > 0)
		{
		?>
		<table id="table">
			<tr>
				<th style="width: 250px;">Категорија на прашање</th>
				<th>Прашање</th>
				<th>Одговор</th>
				<th>Точен</th>
				<th>Внесен од</th>
			</tr>
			<?php
				for($i = 0; $i < count($result); $i++)
				{
					for($j = 0; $j < count($kategorii); $j++)
					{
						if($result[$i]["pid"] == $kategorii[$j]["pid"])
						{
							$kategorija = $kategorii[$j]["body"];
							$prasanje = $result[$i]["body"];
							$pid = $kategorii[$j]["pid"];
							$qid = $result[$i]["qid"];
							$answers = $questons->getAllAnswers($qid, $pid);
							for($k = 0; $k < count($answers); $k++)
							{
							?>
								<tr>
								<td>
									<?php echo $kategorija; ?>
								</td>
								<td>
									<?php echo $prasanje; ?>
								</td>
								<td>
									<?php echo $answers[$k]["body"];?>
								</td>
								<td>
									<?php if($answers[$k]["correct"] == 1)
									{
										echo 'Да';
									}
									else {
										echo 'Не';
									}
									?>
								</td>
								<td>
									<?php
										echo $user->getNameById($answers[$k]["user_id"]);
									?>
								</td>
							</tr>
						<?php
							}
						}
					}
				}
			}
			?>
			</table>
		<?php
		if(count($result) == 0)
			{
				echo "<h1 style='width:100%; text-align:center;'>Во моментов нема внесено прашања.</h1>";
			}
			?>
	</div>
</body>
</html>	