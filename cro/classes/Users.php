<?php

class Users
{
	protected $pdo;

	function __construct($pdo)
	{
		$this->pdo = $pdo;
	}

	public function addNewUser($name, $username, $password)
	{
		$pass_hash = md5($password);
		
		$stmt = $this->pdo->prepare("INSERT INTO users(name, username, password, status, numberasked, created_at) VALUES (:name, :username, :pass, 1, 0, NOW()) ");

		$stmt->bindParam(':name', $name);
		$stmt->bindParam(':username', $username);
		$stmt->bindParam(':pass', $pass_hash);
		if($stmt->execute())
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}

	public function loginUser($username, $password)
	{
		$stmt1 = $this->pdo->prepare("SELECT password FROM users WHERE username=:username");
		$stmt1->bindParam(':username', $username);
		$stmt1->execute();
		$result = $stmt1->fetch(PDO::FETCH_ASSOC);
		$pass_hash = $result["password"];
		$enteredPassword = md5($password);

		if($enteredPassword === $pass_hash)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public function getNameByUsername($username)
	{
		$stmt = $this->pdo->prepare("SELECT name FROM users WHERE username = :username");
		$stmt->bindParam(":username", $username);
		if($stmt->execute())
		{
			$result = $stmt->fetchColumn();
			$stmt->closeCursor();
			return $result;
		}
		else
		{
			return NULL;
		}
	}

	public function getUserByUsername($username)
	{
		$stmt = $this->pdo->prepare("SELECT * FROM users WHERE username = :username");
		$stmt->bindParam(":username", $username);
		if($stmt->execute())
		{	
			$result = $stmt->fetch(PDO::FETCH_ASSOC);
			$stmt->closeCursor();
			return $result;
		}
		else
		{
			return NULL;
		}
	}

	public function getUserById($id)
	{
		$stmt = $this->pdo->prepare("SELECT * FROM users WHERE id = :id");
		$stmt->bindParam(":id", $id);
		if ($stmt->execute()) {
			$result = $stmt->fetch(PDO::FETCH_ASSOC);
			$stmt->closeCursor();
			return $result;
		}
		else
		{
			return NULL;
		}
	}

	public function getNameById($id)
	{
		$stmt = $this->pdo->prepare("SELECT name FROM users WHERE id = :id");
		$stmt->bindParam(":id", $id);
		$stmt->execute();
		$result = $stmt->fetchColumn();
		return $result;
	}

	public function getCount($id)
	{
		$stmt = $this->pdo->prepare("SELECT numberasked FROM users WHERE id = :id");
		$stmt->bindParam(":id", $id);
		$stmt->execute();
		$result = $stmt->fetchColumn(0);
		return $result;
	}

	public function incCount($id)
	{
		$stmt = $this->pdo->prepare("SELECT numberasked FROM users WHERE id = :id");
		$stmt->bindParam(":id", $id);
		$stmt->execute();
		$result = $stmt->fetchColumn(0);
		$stmt->closeCursor();

		$result += 1;
		$stmt = $this->pdo->prepare("UPDATE users SET numberasked = :numberasked WHERE id = :id");
		$stmt->bindParam(":numberasked", $result);
		$stmt->bindParam(":id", $id);
		if($stmt->execute())
		{
			$stmt->closeCursor();
			return true;
		}
		else
		{
			$stmt->closeCursor();
			return false;
		}
	}

	public function getAllUsers()
	{
		$query = $this->pdo->query("SELECT * FROM users");
		return $query->fetchAll(PDO::FETCH_ASSOC);
	}
}