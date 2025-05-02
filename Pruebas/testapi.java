import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class testapi {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Escribe el código del producto a buscar: ");
        String codigo = scanner.nextLine().trim();
        scanner.close();

        try {
            URI uri = new URI("http://localhost/apis/buscar_producto.php?codigo=" + codigo);
            URL url = uri.toURL();        

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            BufferedReader reader;
            if (status >= 200 && status < 300) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder responseStr = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseStr.append(line);
            }
            reader.close();
            conn.disconnect();

            if (!responseStr.toString().isEmpty()) {
                try {
                    JSONObject json = new JSONObject(responseStr.toString());

                    if (json.getInt("status") == 200 && json.has("data")) {
                        JSONObject producto = json.getJSONObject("data");
                        System.out.println("Producto encontrado:");
                        System.out.println("Nombre: " + producto.getString("nombre"));
                        System.out.println("Precio: " + producto.getString("precio"));
                        System.out.println("Imagen: " + producto.getString("imagen"));
                    } else {
                        String mensaje = json.has("mensaje") ? json.getString("mensaje") : "Producto no encontrado";
                        System.out.println("Error: " + json.opt("status") + " - " + mensaje);
                    }

                } catch (Exception e) {
                    System.out.println("Error: Respuesta no válida del servidor (no es JSON)");
                }
            } else {
                System.out.println("Error: La respuesta está vacía.");
            }

        } catch (Exception e) {
            System.out.println("Error de conexión o solicitud: " + e.getMessage());
        }
    }
}
