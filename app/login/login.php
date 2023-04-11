<?php
session_start();

if (isset($_SESSION["users"])) {
    header("Location:index.php");
    exit;
}

if (isset($_GET["user_name"]) && isset($_GET["password"])) {
    $users = $_GET["user_name"];
    $pass = $_GET["password"];

    //Credenciales para establecer conexión 
    $servername = "db";
    $username = "root";
    $password = "0808";
    $dbname = "game";

    // Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    $sql = "SELECT * from users where user_name='$users' and password='$pass'";
    $result = $conn->query($sql);

    //Cogemos el ID del usuario que ha iniciado sesión para operaciones posteriores
    if ($result->num_rows > 0) {
        $_SESSION["users"] = $users;
        header("Location:index.php");
        exit;
    } else {
        $error = "Usuario o contraseña incorrectos";
    }
}
?>

<html>
<head>
    <title> Login </title>
    <meta charset="utf-8">
</head>
<body>
    <?php if (isset($error)) { ?>
        <p style="color:red;"><?php echo $error; ?></p>
    <?php } ?>
    <form action="login.php" method="get">
        <label for="user_name">Usuario :</label><br>
        <input type="text" id="user_name" name="user_name"><br>
        <label for="password">Contraseña :</label><br>
        <input type="password" id="password" name="password"><br><br>
        <input type="submit" value="Submit">
    </form>
</body>
</html>
