<?php
session_start();
include '../classes/Questions.php';
$pdo = require '../config/bootstrap.php';

$question = new Questions($pdo);

$pid = $_POST['option'];
$body = $_POST['prasanje'];
$user_id = $_SESSION['id'];
$correct = $_POST['tocen'];
$gresen1 = $_POST['gresen1'];
$gresen2 = $_POST['gresen2'];

if(!is_null($qid = $question->addQuestion($pid, $body, $user_id)))
{
	if($question->addAnswer($pid, $qid, '1', $correct, $user_id))
	{
		if($question->addAnswer($pid, $qid, '0', $gresen1, $user_id))
		{
			if($question->addAnswer($pid, $qid, '0', $gresen2, $user_id))
			{
				header("location: addQuestion.php?msg=success");
			}
		}
	}

}

