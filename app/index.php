<?php
    session_start();
    if (isset($_SESSION["users"])) {
        //Aquí va todo el contenido de la página de index.php
?>
<!DOCTYPE html>
<html>
<head>
    <title>Index</title>
</head>
<body>
    <!-- Contenido de la página -->
</body>
</html>
<?php
    } else {
        header("Location:/login");
    }
?>
