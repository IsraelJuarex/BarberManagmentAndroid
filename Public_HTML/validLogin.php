<?php
include('./DataBaseConfig.php');

// Open the database connection
$conn = OpenCon();

// Make sure the user input is sanitized to prevent SQL injection
$user = mysqli_real_escape_string($conn, $_GET['usuario']);
$pass = mysqli_real_escape_string($conn, $_GET['pass']);

$query = "SELECT * FROM trabajadores WHERE `usuario` = '$user' AND `contrasena` = '$pass'";

$results = mysqli_query($conn, $query);
$val = array();

if (mysqli_num_rows($results) > 0) {
    while ($res = mysqli_fetch_assoc($results)) {
        $val['response'] = "Success";
    }
    // Call handleSuccess function
    handleSuccess($val, $conn, $user);
} else {
    $val['response'] = "Error";
    // Call handleError function
    handleError($val, $conn);
}

// Close the database connection (moved outside of the if-else block)
CloseCon($conn);
?>
