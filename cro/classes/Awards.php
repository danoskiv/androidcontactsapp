<?php

class Awards
{
	protected $pdo;

	function __construct($pdo)
	{
		$this->pdo = $pdo;
	}

	public function addAward($name, $value, $pid)
	{
		$statement = $this->pdo->prepare("INSERT INTO awards(name, value, pid) VALUES(:name, :value, :pid)");
		$statement->bindParam(":name", $name);
		$statement->bindParam(":value", $value);
		$statement->bindParam(":pid", $pid);

		if($statement->execute())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public function getAwardByName($name)
	{
		$statement = $this->pdo->prepare("SELECT * FROM awards WHERE name = :name");
		$statement->bindParam(":name", $name);

		if($statement->execute())
		{
			$result = $statement->fetchAll(PDO::FETCH_ASSOC);
			return $result;
		}
		else
		{
			return NULL;
		}
	}

	public function getAwardValue($name)
	{
		$statement = $this->pdo->prepare("SELECT value FROM awards WHERE name = :name");
		$statement->bindParam(":name", $name);

		if($statement->execute())
		{
			$result = $statement->fetchAll(PDO::FETCH_ASSOC);
			return $result;
		}
		else
		{
			return NULL;
		}	
	}

	public function getAllAwards()
	{
		$query = $this->pdo->query("SELECT * FROM awards");
		if($query)
			return $query->fetchAll(PDO::FETCH_ASSOC);
		else
			return NULL;
	}

	public function updateAward($id, $name, $value, $cat)
	{
		$statement = $this->pdo->prepare("UPDATE awards SET name=:name, value=:value, pid=:pid WHERE id=:id");
		$statement->bindParam(":name", $name);
		$statement->bindParam(":value", $value);
		$statement->bindParam(":pid", $cat);
		$statement->bindParam(":id", $id);

		if($statement->execute()) {
			return true;
		}
		else {
			return false;
		}
	}
}