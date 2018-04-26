<?php
$pdo = require 'config/bootstrap.php';
require_once('mysql_table.php');

class PDF extends PDF_MySQL_Table
{
function Header()
{
    // Title
    $this->AddFont('MACCTimes','','MCTIME.php');
	$this->SetFont('MACCTimes','',18);
    $this->Cell(0,6, iconv('windows-1252','UTF-8', "Kontakti") ,0,1,'C');
    $this->Ln(10);
    // Ensure table header is printed
    parent::Header();
}
}

// Connect to database
$link = mysqli_connect('localhost','root','','cro_app');
$pdf = new PDF();
$pdf->AddPage();
// First table: output all columns
$pdf->Table($link,'select id, name, email, number from contacts');
$pdf->AddPage();
$pdf->Output();
?>