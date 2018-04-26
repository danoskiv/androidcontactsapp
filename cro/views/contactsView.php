<?php
session_start();

$pdo = require '../config/bootstrap.php';
include '../classes/Contacts.php';
include '../classes/Users.php';

$user = new Users($pdo);
$contact = new Contacts($pdo);

$user2 = new Users($pdo);

$result = $contact->getAllContacts();

?>

<!DOCTYPE html>
<html>
<head>
	<link rel=stylesheet href="../styles/style.css">
	<title>Контакти</title>
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
				<th>Име и презиме</th>
				<th>E-mail адреса</th>
				<th>Тел. број</th>
				<th>Внесен од</th>
			</tr>
			<?php
				for($i = 0; $i < count($result); $i++)
				{
					$user2 = $user->getNameById(intval($result[$i]["user_id"]));
			?>
			<tr>
				<td>
					<?php echo $result[$i]["name"]; ?>
				</td>
				<td>
					<?php echo $result[$i]["email"]; ?>
				</td>
				<td>
					<?php echo $result[$i]["number"]; ?>
				</td>
				<td>
					<?php echo $user2; ?>
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
				echo "<h1 style='width:100%; text-align:center;'>Во моментов нема внесено контакти.</h1>";
			}
			?>
	</div>
</body>
</html>