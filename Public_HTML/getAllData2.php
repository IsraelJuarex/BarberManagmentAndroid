<?php
include('./validLogin.php');

function handleSuccess($val, $conn) {

    $query = "SELECT * FROM trabajadores WHERE admin = 0";
    $results = mysqli_query($conn, $query);
    $vas = array();

    if (mysqli_num_rows($results) > 0) {
        while ($res = mysqli_fetch_assoc($results)) {
            
            $vas[] = $res;
        }
        echo json_encode($vas);
    }
}

function handleError($val, $conn) {
    echo json_encode(array("response" => "Error"));
}


?>