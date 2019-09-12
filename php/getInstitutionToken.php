<?php

    include_once("include/dbconfig.php");

    $data = json_decode(file_get_contents('php://input'), true);

    $get_token= mysqli_query($connect, "SELECT `id`, `username`, `email`, `password`, `token`, `institution`, `date_created` FROM `responder` WHERE institution='{$data['institution']}'");
   
    if(mysqli_num_rows($get_token) > 0){
        
        $row= mysqli_fetch_array($get_token);
        $output = array('token' => $row["token"]);

        echo json_encode($output);
    }
?>