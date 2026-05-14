package HTTPServer.codefiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost", 7070);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            printWriter.println("GET /hello HTTP/1.1");
            printWriter.println("Host: localhost");
            printWriter.println();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

        }catch (IOException exception) {
            throw new RuntimeException("Client Communication Failed");
        }

    }
}
