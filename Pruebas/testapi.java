import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class testapi {
    public static void main(String args[]) {

        System.out.println("Escribe el codigo del producto a buscar: ");

        Scanner sc = new Scanner(System.in);

        String codigo = sc.nextLine();

        String url = "http://localhost/apis/buscar_producto.php?codigo=" + codigo;

        System.out.println(url);

        // llamamos a la api

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("Response Code 1: " + responseCode);

            // if (responseCode == HttpURLConnection.HTTP_OK) {
            if (responseCode == 500) {

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Response 2: " + response.toString());
            } else {
                System.out.println("GET request failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}