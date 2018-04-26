<?php

class Check
{
	public static function checkAdmin()
	{
		if(isset($_SESSION['logged_in']) && isset($_SESSION['user']) && isset($_SESSION['id']) && $_SESSION['id'] == 1)
		{
			return true;	
		}
		else
		{
			return false;
		}
	}

	public static function checkUser()
	{
		if(isset($_SESSION['logged_in']) && isset($_SESSION['user']) && isset($_SESSION['id']))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}