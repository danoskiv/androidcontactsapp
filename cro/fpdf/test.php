<?php
require('fpdf.php');

$pdf = new FPDF();
$pdf->AddFont('MACCTimes','','MCTIME.php');
$pdf->AddPage();
$pdf->SetFont('MACCTimes','',35);
$str = 'Владимир';
$str = iconv("ISO-8859-5", "UTF-8", $str);
$str = utf8_decode($str);
$pdf->Write(10,'Enjoy new fonts with FPDF!' . $str);
$pdf->Output();
?>