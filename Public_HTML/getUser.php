<?php
include('./DataBaseConfig.php');

// Open the database connection
$conn = OpenCon();

// Make sure the user input is sanitized to prevent SQL injection mysqli_real_escape_string($conn, $_POST['user']);
$user = mysqli_real_escape_string($conn, $_GET['usuario']);


$query = "SELECT * FROM trabajadores WHERE `usuario` = '$user'";
$results = mysqli_query($conn, $query);
$val = array();

if (mysqli_num_rows($results) > 0) {
    while ($res = mysqli_fetch_assoc($results)) {
        //$val['response'] = "Success";
        $val[] = $res;
    }
    echo json_encode($val);
} else {
    //$val['response'] = "There is no data";
    echo json_encode($val);
}

// Close the database connection
CloseCon($conn);
?>