<?php

$pdo = require '../config/bootstrap.php';
include '../classes/Awards.php';

$award = new Awards($pdo);

$json = $award->getAllAwards();

header('Content-Type: application/json; charset=UTF-8');
echo json_encode($json);