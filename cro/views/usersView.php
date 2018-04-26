<?php
session_start();

$pdo = require '../config/bootstrap.php';
include '../classes/Users.php';

$user = new Users($pdo);

$result = $user->getAllUsers();

?>

<!DOCTYPE html>
<html>
<head>
	<link rel=stylesheet href="../styles/style.css">
	<title>Корисници</title>
</head>
<body>
	<?php require 'partialHeader.php'; ?>
	<div id="divTable">
		<table id="table">
			<tr>
				<th>Име и презиме</th>
				<th>Корисничко име</th>
				<th>Статус</th>
				<th>Број на испитаници</th>
				<th>Креиран на</th>
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
					<?php echo $result[$i]["username"]; ?>
				</td>
				<td>
					<?php echo $result[$i]["status"]; ?>
				</td>
				<td>
					<?php echo $result[$i]["numberasked"]; ?>
				</td>
				<td>
					<?php echo $result[$i]["created_at"]; ?>
				</td>
			</tr>
			<?php
				}
			?>
		</table>
	</div>
</body>
</html>