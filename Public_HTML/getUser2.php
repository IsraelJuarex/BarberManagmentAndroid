<?php
include('./validLogin.php');

$conn = OpenCon();

// Make sure the user input is sanitized to prevent SQL injection
$user = mysqli_real_escape_string($conn, $_GET['usuario']);

function handleSuccess($val, $conn, $user) {
    $query = "SELECT * FROM trabajadores WHERE `usuario` = '$user'";
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

// Close the database connection
CloseCon($conn);
?>
