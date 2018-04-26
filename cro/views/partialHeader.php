<?php
session_start();
$name = $_SESSION['name'];
?>
<header>
	<div style="float: left; padding: 13px;">
		<label style="color: #fff;">Здраво <?php echo $name; ?></label>
		<a id="pocetna" href="home.php">Почетна</a>
	</div>
	<div style="float: right; padding-right: 15px;">
		 <div class="dropdown">
		 	<span class="dropbtn">
		 		Повеќе
		 	</span>
		 	<div class="dropdown-content">
		    	<a href="../methods/addUser.php">Додади корисник</a>
		    	<a href="../methods/addQuestion.php">Додади прашање</a>
		    	<a href="../methods/addAward.php">Додади награди</a>
		  	</div>
		</div> 
		<nav>
			<a href="../views/usersView.php">Корисници</a>
			<a href="../views/questionsView.php">Прашања</a>
			<a href="../views/contactsView.php">Контакти</a>
			<a href="../views/awardsView.php">Награди</a>
			<a href="../methods/logout.php">Одјави се</a>
		</nav>
	</div>
</header>