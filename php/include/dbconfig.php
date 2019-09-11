<?php

define("DB_HOST","localhost");
define("DB_USER","root");
define("DB_PASSWORD","root");
define("DB_DATABASE","emergencyresponder");

$con = mysqli_connect(DB_HOST,DB_USER,DB_PASSWORD,DB_DATABASE);

if (mysqli_connect_errno()) {

    die("Database connection failed"."{".mysqli_connect_errno()." - ".mysqli_connect_errno()."}");
}






?>