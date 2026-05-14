package HTTPServer.codefiles;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Server {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(7070)) {
            System.out.println("Server Started");
            do {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(
                        () -> handleRequest(clientSocket)
                );
                clientThread.start();
            }while (true);

        } catch (IOException e) {
            throw new RuntimeException("Server Failed to Start");
        }
    }

    static void handleRequest(Socket clientSocket) {
        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream outputStream = clientSocket.getOutputStream();
                ) {
            String requestLine = bufferedReader.readLine();

            if (requestLine == null) return;
            System.out.println("Request: " + requestLine);

            String[] parts = requestLine.split(" ");
            String method = parts[0], path = parts[1];

            String line;
            while (!(line = bufferedReader.readLine()).isEmpty()) {
                System.out.println("Header: " + line);
            }
            String responseBody;
            int statusCode;

            if (path.equals("/hello")) {
                responseBody = "Hello from bare metal HTTP server";
                statusCode = 200;
            } else if (path.equals("/time")) {
                responseBody = new Date().toString();
                statusCode = 200;
            } else {
                responseBody = "404 Not Found";
                statusCode = 404;
            }

            outputStream.write(("HTTP/1.1 " + statusCode + " OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: " + responseBody.getBytes().length + "\r\n" +
                    "\r\n" +
                    responseBody).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            clientSocket.close();

        } catch (IOException e) {
            throw new RuntimeException("Failed Read/Write From Client");
        }
    }
}
