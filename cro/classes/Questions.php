<?php

class Questions
{
	protected $pdo;

	function __construct($pdo)
	{
		$this->pdo = $pdo;
	}

	public function addParent($body, $user_id)
	{
		$query = $this->pdo->query("SELECT pid FROM questions ORDER BY pid DESC LIMIT 1")->fetchColumn();
		if(!is_null($query))
		{
			$pid = $query + 1;
			$statement = $this->pdo->prepare("INSERT INTO questions(pid, body, created_at, user_id) VALUES(:pid, :body, NOW(), :user_id)");
			$statement->bindParam(":pid", $pid);
			$statement->bindParam(":body", $body);
			$statement->bindParam(":user_id", $user_id);

			if($statement->execute())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			$pid = 1;
			$statement = $this->pdo->prepare("INSERT INTO questions(pid, body, created_at, user_id) VALUES(:pid, :body, NOW(), :user_id)");
			$statement->bindParam(":pid", $pid);
			$statement->bindParam(":body", $body);
			$statement->bindParam(":user_id", $user_id);

			if($statement->execute())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	public function addQuestion($pid, $body, $user_id)
	{
		$statement = $this->pdo->prepare("SELECT qid FROM questions WHERE pid = :pid ORDER BY qid DESC LIMIT 1");
		$statement->bindParam(":pid", $pid);
		if($statement->execute())
		{
			$qid = $statement->fetchColumn();
			if(!is_null($qid))
			{
				$qid += 1;
				$statement2 = $this->pdo->prepare("INSERT INTO questions(pid, qid, body, created_at, user_id) VALUES(:pid, :qid, :body, NOW(), :user_id)");
				$statement2->bindParam(":pid", $pid);
				$statement2->bindParam(":qid", $qid);
				$statement2->bindParam(":body", $body);
				$statement2->bindParam(":user_id", $user_id);

				if($statement2->execute())
				{
					return $qid;
				}
				else
				{
					return NULL;
				}
			}
			else
			{
				$qid = 1;
				$statement2 = $this->pdo->prepare("INSERT INTO questions(pid, qid, body, created_at, user_id) VALUES(:pid, :qid, :body, NOW(), :user_id)");
				$statement2->bindParam(":pid", $pid);
				$statement2->bindParam(":qid", $qid);
				$statement2->bindParam(":body", $body);
				$statement2->bindParam(":user_id", $user_id);

				if($statement2->execute())
				{
					return $qid;
				}
				else
				{
					return NULL;
				}
			}
		}
	}

	public function addAnswer($pid, $qid, $correct, $body, $user_id)
	{
		$statement = $this->pdo->prepare("SELECT aid FROM questions WHERE pid = :pid AND qid = :qid ORDER BY aid DESC LIMIT 1");
		$statement->bindParam(":pid", $pid);
		$statement->bindParam(":qid", $qid);
		$statement->execute();
		$aid = $statement->fetchColumn();
		if(!is_null($aid))
		{
			$aid += 1;
			$statement2 = $this->pdo->prepare("INSERT INTO questions(pid, qid, aid, correct, body, created_at, user_id) VALUES(:pid, :qid, :aid, :correct, :body, NOW(), :user_id)");
			$statement2->bindParam(":pid", $pid);
			$statement2->bindParam(":qid", $qid);
			$statement2->bindParam(":aid", $aid);
			$statement2->bindParam(":correct", $correct);
			$statement2->bindParam(":body", $body);
			$statement2->bindParam(":user_id", $user_id);

			if($statement2->execute())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			$aid = 1;
			$statement2 = $this->pdo->prepare("INSERT INTO questions(pid, qid, aid, correct, body, created_at, user_id) VALUES(:pid, :qid, :aid, :correct, :body, NOW(), :user_id)");
			$statement2->bindParam(":pid", $pid);
			$statement2->bindParam(":qid", $qid);
			$statement2->bindParam(":aid", $aid);
			$statement2->bindParam(":correct", $correct);
			$statement2->bindParam(":body", $body);
			$statement2->bindParam(":user_id", $user_id);

			if($statement2->execute())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	public function getAllParents()
	{
		$query = $this->pdo->query("SELECT pid, body FROM questions WHERE pid IS NOT NULL AND qid IS NULL AND aid IS NULL");
		if($query)
		{
			$result = $query->fetchAll(PDO::FETCH_ASSOC);
			return $result;
		}
		else
		{
			return NULL;
		}
	}

	public function getRandomQuestion($pid)
	{
		//get the number of distinct questions
		$statement = $this->pdo->prepare("SELECT COUNT(DISTINCT qid) FROM questions WHERE pid = :pid");
		$statement->bindParam(":pid", $pid);
		if($statement->execute())
		{
			$number = $statement->fetchColumn();
			$random = rand(1, $number);
			return $random;
		}
		else
		{
			return NULL;
		}
	}

	public function getAllCategories()
	{
		$query = $this->pdo->query("SELECT pid, body FROM questions WHERE qid IS NULL AND aid IS NULL");
		if($query)
		{
			return $query->fetchAll(PDO::FETCH_ASSOC);
		}
		else
		{
			return NULL;
		}
	}

	public function getAllQuestionsByCategory($pid)
	{
		$statement = $this->pdo->prepare("SELECT qid, body FROM questions WHERE pid = :pid AND ");
		$statement->bindParam(":pid", $pid);
		$statement->execute();
		$result = $statement->fetchAll(PDO::FETCH_ASSOC);
		return $result;
	}

	public function getAllAnswers($qid, $pid)
	{
		$statement = $this->pdo->prepare("SELECT aid, body, correct, user_id FROM questions WHERE qid = :qid AND pid = :pid AND aid IS NOT NULL");
		$statement->bindParam(":qid", $qid);
		$statement->bindParam(":pid", $pid);
		$statement->execute();
		return $statement->fetchAll(PDO::FETCH_ASSOC);
	}

	public function getAllQuestions()
	{
		$query = $this->pdo->query("SELECT * FROM questions WHERE qid IS NOT NULL AND pid IS NOT NULL AND aid IS NULL");
		if($query)
			return $query->fetchAll(PDO::FETCH_ASSOC);
		else
			return NULL;
	}

	public function getCompleteQuestions()
	{
		$query = $this->pdo->query("SELECT * FROM questions");
		if($query)
			return $query->fetchAll(PDO::FETCH_ASSOC);
		else
			return NULL;
	}

	public function getCategoryById($pid)
	{
		$statement = $this->pdo->prepare("SELECT body FROM questions WHERE pid = :pid AND qid IS NULL and aid IS NULL");
		$statement->bindParam(":pid", $pid);
		$statement->execute();
		$result = $statement->fetchColumn();
		return $result;
	}
}