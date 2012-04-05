<?php

$un=$_POST['username'];

$pw=$_POST['password'];

//connect to the db

$user = 'root';

$pswd = '';

$db = 'science';

$conn = mysql_connect('localhost', $user, $pswd);

mysql_select_db($db, $conn);

//run the query to search for the username and password the match

$query = "SELECT * FROM users WHERE username = '$un' AND password = '$pw'";

$result = mysql_query($query) or die("Unable to verify user because : " . mysql_error());

//this is where the actual verification happens

if(mysql_num_rows($result) == 1)

echo 1;  // for correct login response

else

echo 0; // for incorrect login response
?>