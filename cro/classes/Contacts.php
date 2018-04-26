<?php

class Contacts
{
	protected $pdo;

	function __construct($pdo)
	{
		$this->pdo = $pdo;
	}

	public function insertContacts($contacts, $user_id)
	{
		for($i = 0; $i < count($contacts); $i++)
		{
			$statement = $this->pdo->prepare("INSERT INTO contacts(name, email, number, user_id) VALUES(:name, :email, :number, :user_id)");
			$statement->bindParam(":name", $contacts[$i]["name"]);
			$statement->bindParam(":email", $contacts[$i]["email"]);
			$statement->bindParam(":number", $contacts[$i]["number"]);
			$statement->bindParam(":user_id", $user_id);
			if($statement->execute())
			{
				return true;
			}
			else
				return false;
		}
	}

	public function getAllContacts()
	{
		$query = $this->pdo->query("SELECT * FROM contacts");
		if($query)
			return $query->fetchAll(PDO::FETCH_ASSOC);
		else
			return NULL;
	}

	public function addContact($id, $name, $email, $phone, $user_id)
	{
		$statement = $this->pdo->prepare("INSERT INTO contacts(id_from_local, name, email, number, created_at, user_id) VALUES(:id_from_local, :name, :email, :phone, NOW(), :user_id)");

		$statement->bindParam(":id_from_local", $id);
		$statement->bindParam(":name", $name);
		$statement->bindParam(":email", $email);
		$statement->bindParam(":phone", $phone);
		$statement->bindParam(":user_id", $user_id);

		if($statement->execute())
		{
			return true;
		}
		else {
			return false;
		}
	}
}