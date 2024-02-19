<?php
include('./DataBaseConfig.php');

// Open the database connection
$conn = OpenCon();


$query = "SELECT * FROM trabajadores where admin = 0";
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