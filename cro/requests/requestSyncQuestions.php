<?php

$pdo = require '../config/bootstrap.php';
include '../classes/Questions.php';

$question = new Questions($pdo);
$json = $question->getCompleteQuestions();

header('Content-Type: application/json');
echo json_encode($json);

