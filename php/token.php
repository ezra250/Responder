<?php 

    require_once 'include/dbconfig.php';

    $data = json_decode(file_get_contents('php://input'), true);

    $get_tokens= mysqli_query($connect, "SELECT `id` FROM `responder` WHERE `id`='{$data['id']}'");
   
    if(mysqli_num_rows($get_tokens) > 0){
        //update token
        $sql=mysqli_query($connect, "UPDATE `responder` SET `token`='{$data['token']}' WHERE `id`='{$data['id']}'");
    }
?>