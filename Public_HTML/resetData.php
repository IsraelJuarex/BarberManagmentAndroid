<?php
include('./DataBaseConfig.php');


// Open the database connection
$conn = OpenCon();

$sql = "UPDATE trabajadores SET cortes = DEFAULT, barbas = DEFAULT, cejas = DEFAULT , total= DEFAULT";
            

if ($conn->query($sql) == TRUE) {
  echo "Record updated successfully";
} else {
  echo "Error updating record: " . $conn->error;
}

$conn->close();
?>
