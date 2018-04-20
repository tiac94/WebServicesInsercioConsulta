<?php
//$ip = $_GET['ip'];
$id = mysql_connect("localhost", "root","");
$conn = mysql_select_db("Rutes", $id);
$res = mysql_query("SELECT * FROM RUTES");
$json = array();
while ($fila = mysql_fetch_array($res))
    $json[] = $fila;
echo json_encode($json);

?>
