<?php

$un=$_POST['username'];

//connect to the db

$user = 'root';

$pswd = '';

$db = 'science';

$conn = mysql_connect('localhost', $user, $pswd);

mysql_select_db($db, $conn);

//run the query to search for the username and password the match

$query = "SELECT id FROM users WHERE username = '$un'";

$result = mysql_query($query) or die("Unable to verify user because : " . mysql_error());

//this is where the actual verification happens

$row = mysql_fetch_assoc($result); 
echo ($row['id']);  

//if(mysql_num_rows($result) == 1)

//echo (mysql_fetch_row($result));

// $result;  // for correct login response

//else

//echo 0; // for incorrect login response
?>