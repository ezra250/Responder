<?php require_once("include/dbconfig.php");?>
<?php

    function storeUser($username, $email, $password){
        global $con;
        
        $query = "INSERT INTO responder(";
        $query .= "username, email, password) ";
        $query .= "VALUES('{$username}', '{$email}','{$password}')";

        $result = mysqli_query($con, $query);

        if($result){
            $user = "SELECT * FROM responder WHERE email = '{$email}'";
            $res = mysqli_query($con, $user);

            while ($user = mysqli_fetch_assoc($res)){
                return $user;
            }
        }else{
                return false;
            }

    }


    function getUserByEmailAndPassword($email, $password){
        global $con;
        $query = "SELECT * from responder where email = '{$email}' and password = '{$password}'";
    
        $user = mysqli_query($con, $query);
        
        if($user){
            while ($res = mysqli_fetch_assoc($user)){
                return $res;
            }
        }
        else{
            return false;
        }
    }


    function emailExists($email){
        global $con;
        $query = "SELECT email from responder where email = '{$email}'";

        $result = mysqli_query($con, $query);

        if(mysqli_num_rows($result) > 0){
            return true;
        }else{
            return false;
        }
    }

?>