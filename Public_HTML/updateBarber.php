<?php
include('./DataBaseConfig.php');


// Open the database connection
$conn = OpenCon();

// Make sure the user input is sanitized to prevent SQL injection mysqli_real_escape_string($conn, $_POST['user']);
$user = mysqli_real_escape_string($conn, $_GET['usuario']);
$serv= mysqli_real_escape_string($conn, $_GET['servicio']);

$sql = "UPDATE `trabajadores` SET `$serv` = `$serv` + 1 WHERE `usuario` = '$user'";


if ($conn->query($sql) == TRUE) {
  echo "Record updated successfully";
} else {
  echo "Error updating record: " . $conn->error;
}

$conn->close();
?>
