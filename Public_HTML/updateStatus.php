<?php
include('./DataBaseConfig.php');


// Open the database connection
$conn = OpenCon();

// Make sure the user input is sanitized to prevent SQL injection mysqli_real_escape_string($conn, $_POST['user']);
$user = mysqli_real_escape_string($conn, $_GET['usuario']);
$status = mysqli_real_escape_string($conn, $_GET['status']);


$sql = "UPDATE `trabajadores` SET `status` = '$status' WHERE `usuario` = '$user'";


if ($conn->query($sql) == TRUE) {
  echo "Record updated successfully";
} else {
  echo "Error updating record: " . $conn->error;
}

$conn->close();
?>
