package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"net/http"
	"net/url"
	"os"
)

type Producto struct {
	Nombre string `json:"nombre"`
	Precio string `json:"precio"`
	Imagen string `json:"imagen"`
}

type Respuesta struct {
	Status  int       `json:"status"`
	Mensaje string    `json:"mensaje"`
	Data    *Producto `json:"data"`
}

func main() {
	reader := bufio.NewReader(os.Stdin)
	fmt.Print("Escribe el código del producto a buscar: ")
	codigo, _ := reader.ReadString('\n')
	codigo = url.QueryEscape(codigo[:len(codigo)-1])

	baseURL := "http://localhost/apis/buscar_producto.php"
	reqURL := fmt.Sprintf("%s?codigo=%s", baseURL, codigo)

	resp, err := http.Get(reqURL)
	if err != nil {
		fmt.Printf("Error de conexión o solicitud: %v\n", err)
		return
	}
	defer resp.Body.Close()

	var resultado Respuesta
	err = json.NewDecoder(resp.Body).Decode(&resultado)
	if err != nil {
		fmt.Println("Error: Respuesta no válida del servidor (no es JSON)")
		return
	}

	if resultado.Status == 200 && resultado.Data != nil {
		fmt.Println("Producto encontrado:")
		fmt.Printf("Nombre: %s\nPrecio: %s\nImagen: %s\n", resultado.Data.Nombre, resultado.Data.Precio, resultado.Data.Imagen)
	} else {
		if resultado.Mensaje == "" {
			resultado.Mensaje = "Producto no encontrado"
		}
		fmt.Printf("Error: %d - %s\n", resultado.Status, resultado.Mensaje)
	}
}
