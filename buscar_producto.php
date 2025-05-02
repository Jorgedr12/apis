<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
error_reporting(E_ALL);
ini_set('display_errors', 1);

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    sendResponse(405, "Método no permitido");
}

$codigo = isset($_GET["codigo"]) ? trim($_GET["codigo"]) : null;

if (!$codigo) {
    sendResponse(400, "Bad Request: código de producto no proporcionado");
}

if (!is_numeric($codigo)) {
    sendResponse(400, "Bad Request: el código debe ser numérico");
}

$conexion = connectDatabase();

if (!$conexion) {
    sendResponse(500, "Internal Server Error: error de conexión");
}

$producto = getProducto($conexion, $codigo);

mysqli_close($conexion);

if ($producto) {
    sendResponse(200, "OK: Producto encontrado", $producto);
} else {
    sendResponse(404, "Not Found: Producto no encontrado");
}

function connectDatabase() {
    $conexion = mysqli_connect("localhost", "root", "", "pos");
    return $conexion ?: null;
}

function getProducto($conexion, $codigo) {
    $consulta = "SELECT nombre, precio, imagen FROM productos WHERE id = ?";
    $stmt = mysqli_prepare($conexion, $consulta);
    
    if ($stmt === false) {
        return null;
    }

    mysqli_stmt_bind_param($stmt, 'i', $codigo);
    mysqli_stmt_execute($stmt);
    $resultado = mysqli_stmt_get_result($stmt);

    $producto = mysqli_fetch_assoc($resultado);

    mysqli_stmt_close($stmt);

    return $producto ?: null;
}

function sendResponse($status, $mensaje, $data = null) {
    http_response_code($status);
    $respuesta = [
        "status" => $status,
        "mensaje" => $mensaje
    ];
    if ($data !== null) {
        $respuesta["data"] = $data;
    }
    echo json_encode($respuesta);
    exit;
}
