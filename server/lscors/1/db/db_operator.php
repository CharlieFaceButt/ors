<?php

$mysql_server_name = "";
$mysql_username = "";
$mysql_password = "";
$mysql_database = "";

$conn = mysql_connect($mysql_server_name, $mysql_username, $mysql_password);

$sql = generate_sql();

$result = mysql_db_query($mysql_database, $sql, $conn);

$row = mysql_fetch_row($result);

echo "";

for($i = 0 ; $i < mysql_num_fields($result) ; $i ++){
	mysql_field_name($result, $i);
}

while($row = mysql_fetch_row($result)){
	for($i = 0 ; $i < mysql_num_fields($result) ; $i ++){
		$row[$i];
	}
}

mysql_free_result($result);

mysql_close($conn);

?>