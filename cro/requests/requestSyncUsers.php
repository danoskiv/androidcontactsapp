<?php

$pdo = require '../config/bootstrap.php';
include '../classes/Users.php';

$reg = new Users($pdo);

$json = $reg->getAllUsers();

header('Content-Type: application/json');
echo json_encode($json);

