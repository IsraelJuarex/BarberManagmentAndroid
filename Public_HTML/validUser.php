<?php
function OpenCon()
{
    $dbhost = "roundhouse.proxy.rlwy.net";
    $dbuser = "root";
    $dbpass = "Eh63-6AfGdfAgD56hh-fF1ccdcFCABeh";
    $dbname = "railway";
    $dbport = "48938";// Fix the variable name here
    $conn = new mysqli($dbhost, $dbuser, $dbpass, $dbname, $dbport) or die("Connect failed: %s\n" . $conn->error);
    return $conn;
}

function CloseCon($conn)
{
    $conn->close();
}

?>