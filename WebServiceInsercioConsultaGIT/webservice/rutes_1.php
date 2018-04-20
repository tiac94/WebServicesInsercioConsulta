<?php
$id = mysql_connect("localhost", "root","");
$conn = mysql_select_db("Rutes", $id);
$res = mysql_query("SELECT * FROM RUTES");
while ($fila = mysql_fetch_array($res))
    echo $fila[0] . ".- " . $fila[1] . "<br/>";
?>
