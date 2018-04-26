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
	<title>Ново прашање</title>
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
	<form method="POST" action="postQuestion.php">
		<div class="container">
			<label for="kategorija">Одбери категорија или додај<span id="myBtn"><strong>нова</strong></span>:</label>
			
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
			</select> <br>
			<label for="prasanje">Внесето го прашањето:</label>
			<input type="text" placeholder="Внесето го прашањето" name="prasanje" required>
			<label for="odgovor">Внесете ги одговорите:</label>
			<input type="text" placeholder="Внесете го точниот одговор" name="tocen" required>
			<input type="text" placeholder="Внесете погрешен одговор" name="gresen1" required>
			<input type="text" placeholder="Внесете погрешен одговор" name="gresen2" required>
			<button type="submit">Додај</button>
		</div>
	</form>
	<div id="myModal" class="modal">
		<form method="POST" action="addCategory.php">
			<div class="modal-content">
				<span class="close">&times;</span>
				<label for="kateg">Име на категорија:</label>
				<input type="text" placeholder="Име на категорија" name="kategorija" required>
				<button type="submit">Додај</button>
		</form>
	</div>
	<script>
		var x = document.getElementById('hideMe');
		x.addEventListener("webkitAnimationEnd", myEndFunction);

		function myEndFunction()
		{
			this.style.height = 0;
			this.style.width = 0;
		}
		// Get the modal
		var modal = document.getElementById('myModal');

		// Get the button that opens the modal
		var btn = document.getElementById("myBtn");

		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("close")[0];

		// When the user clicks the button, open the modal 
		btn.onclick = function() {
		    modal.style.display = "block";
		}

		// When the user clicks on <span> (x), close the modal
		span.onclick = function() {
		    modal.style.display = "none";
		}

		// When the user clicks anywhere outside of the modal, close it
		window.onclick = function(event) {
		    if (event.target == modal) {
		        modal.style.display = "none";
		    }
		}
	</script>
</body>
</html>
<?php
}
else
{
	echo "NEUSPESNO";
}