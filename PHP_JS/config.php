<?php
   define('DB_SERVER', 'localhost'); // no "localhost:3306 needed. it wont work with it there.
   define('DB_USERNAME', 'root');
   define('DB_PASSWORD', '');
   define('DB_DATABASE', 'homework_2');
   $db = mysqli_connect(DB_SERVER,DB_USERNAME,DB_PASSWORD,DB_DATABASE);
   if ($db->connect_error) {
		die("Connection failed: " . $db->connect_error);
   }
  // echo "connection successful"; 
?>