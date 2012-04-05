<?php

$user_id=$_POST['user_id'];

$date=$_POST['date'];

$duration=$_POST['duration'];

$type=$_POST['type'];

//connect to the db

$user = 'root';

$pswd = '';

$db = 'science';

$conn = mysql_connect('localhost', $user, $pswd);

mysql_select_db($db, $conn);

//run the query to search for the username and password the match

$query = "INSERT INTO calls_data (user_id, date, duration, type) VALUES ('$user_id', '$date', '$duration', '$type')"; 

$result = mysql_query($query) or die("Unable to verify user because : " . mysql_error());

//this is where the actual verification happens

if($result)

echo 1;  // for correct login response

else

echo 0; // for incorrect login response
?>