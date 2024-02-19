<?php
function OpenCon()
{
    $dbhost = "sql.freedb.tech";
    $dbuser = "freedb_Israel1234";
    $dbpass = "!Tuz??V@C2qq&gv";
    $dbname = "freedb_BarberManagement1";
    $dbport = "3306";// Fix the variable name here
    $conn = new mysqli($dbhost, $dbuser, $dbpass, $dbname, $dbport) or die("Connect failed: %s\n" . $conn->error);
    return $conn;
}

function CloseCon($conn)
{
    $conn->close();
}

?>