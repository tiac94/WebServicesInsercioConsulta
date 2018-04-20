<?php
$id = mysql_connect("localhost", "root","");
$conn = mysql_select_db("Rutes", $id);



	$nom_r = $_GET['nom_r'];
	$desn = $_GET['desn'];
	$desn_acum = $_GET['desn_acum'];
	$success=0;
	
	$res = mysql_query("INSERT INTO `RUTES`(`nom_r`, `desn`, `desn_acum`) VALUES ('". $nom_r. "',".$desn.",".$desn_acum.")");
		
if($res)
{
	$success=1;
}
$response["success"]=$success;
die(json_encode($response));

mysql_close($id);
//$res = mysql_query("INSERT INTO `RUTES`(`nom_r`, `desn`, `desn_acum`) VALUES ('ruuta2',23,23)";
?>
