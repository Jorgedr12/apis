import requests

codigo = input("Escribe el código del producto a buscar: ").strip()

try:
    response = requests.get(
        "http://localhost/apis/buscar_producto.php",
        params={'codigo': codigo}
    )
except requests.exceptions.RequestException as e:
    print(f"Error de conexión o solicitud: {e}")
    exit()

if response.text:
    try:
        data = response.json()
    except ValueError:
        print("Error: Respuesta no válida del servidor (no es JSON)")
        exit()
else:
    print("Error: La respuesta está vacía.")
    exit()

if data.get('status') == 200 and 'data' in data:
    producto = data['data']
    print("Producto encontrado:")
    print(f"Nombre: {producto['nombre']} \nPrecio: {producto['precio']} \nImagen: {producto['imagen']}")
else:
    mensaje = data.get('mensaje', 'Producto no encontrado')
    print(f"Error: {data.get('status', 'Desconocido')} - {mensaje}")
