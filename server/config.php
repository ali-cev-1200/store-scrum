<?php
// Local XAMPP database (see README)
$con = mysqli_connect('localhost', 'root', '', 'dunibazaar') or die("<h2>MySQL connection error!</h2>");
mysqli_set_charset($con, 'utf8mb4');
