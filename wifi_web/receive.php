<?php
  
  $light = $_GET['value'];


  //user information
  $host = "localhost";
  $user = "test123";
  $pass = "test123";

  //database information
  $databaseName = "light";
  $tableName = "light";


  //Connect to mysql database
  $con = mysql_connect($host,$user,$pass);
  $dbs = mysql_select_db($databaseName, $con);


  //Query database for data
    $result = mysql_query("insert into $tableName (data) VALUES ($light)");

  //store matrix
  if($result==1)
    echo "success";
  else
    echo "error";
?>