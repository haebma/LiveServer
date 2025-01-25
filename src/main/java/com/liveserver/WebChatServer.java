package com.liveserver;

import java.io.*;
import java.net.*;

//TODO: - Browser holt zusätzliches Script und favicon nicht, warum?? Im HTML file was ändern?
//      - Threadpool starten und neuen Thread pro User starten
//      - sleeps oder mit Signal aufwecken einbauen, beim accepten von neuen Clients (oder blockiert accept energieeffizient??) um Ressourcen zu sparen -> s. SP
//      - HTML-Tools implementieren/portieren
public class WebChatServer {
    private static int port = 8080;
    private static String indexHtmlFile = "src/main/resources/static/index.html";
    private static String creditsHtmlFile = "src/main/resources/static/credits.html";
    private static String cssFile = "src/main/resources/static/styles.css";
    private static String jsFile = "src/main/resources/static/script.js";
    private static String faviconFile = "src/main/resources/static/favicon.png";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port)) { // try-with-resources automatically closes resources after leaving the block
            System.out.println("Server running at http://localhost:" + port);
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String requestLine = reader.readLine();
                    System.out.println("Received request: " + requestLine);

                    if (requestLine != null && requestLine.startsWith("GET")) {
                        // TODO: replace with switch-case
                        if (requestLine.contains(".css")) {
                            sendOKandFile(writer, cssFile);
                        } else if (requestLine.contains(".js")) {
                            sendOKandFile(writer, jsFile);
                        } else if (requestLine.contains("favicon.ico")) {
                            sendOKandFile(writer, faviconFile);
                        } else if (requestLine.contains("credits.html")){
                                sendOKandFile(writer, creditsHtmlFile);
                        } else {
                            sendOKandFile(writer, indexHtmlFile);
                        }
                    } else {
                        sendBadRequest(writer);
                    }
                } catch (IOException e) {
                    System.err.println("Error in processing: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Couldn't start server: " + e.getMessage());
        }
    }

    private static void sendFile(PrintWriter writer, String filepath) throws IOException{
        try (FileInputStream fileInputStream = new FileInputStream(filepath)){
            byte[] buffer = new byte[1024]; // 1kB best compromise between speed and memory usage
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                writer.write(new String(buffer, 0, bytesRead));
            }
            writer.println();
        } catch (FileNotFoundException e) {
            System.err.println("Error in processing: " + e.getMessage());
        }
    }
    private static void sendOKandFile(PrintWriter writer, String filepath) {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: " + getContentType(filepath));
        writer.println("Connection: close");
        writer.println("");
        try {
            sendFile(writer, filepath);
        } catch (FileNotFoundException e) {
            sendNotFound(writer);
        } catch (IOException e) {
            System.err.println("Error in processing: " + e.getMessage());
            sendInternalServerError(writer);
        }
    }

    private static String getContentType(String filepath) {
        if (filepath.endsWith(".html")) {
            return "text/html";
        } else if (filepath.endsWith(".css")) {
            return "text/css";
        } else if (filepath.endsWith(".js")) {
            return "text/javascript";
        } else {
            return "text/plain";
        }
    }

    private static void sendBadRequest(PrintWriter writer) {
        writer.println("HTTP/1.1 400 Bad Request");
        writer.println("Content-Type: text/plain");
        writer.println("Connection: close");
        writer.println("");
        writer.println("400 - Bad Request");
    }

    private static void sendNotFound(PrintWriter writer) {
        writer.println("HTTP/1.1 404 Not Found");
        writer.println("Content-Type: text/plain");
        writer.println("Connection: close");
        writer.println("");
        writer.println("404 - Not Found");
    }

    private static void sendInternalServerError(PrintWriter writer) {
        writer.println("HTTP/1.1 500 Internal Server Error");
        writer.println("Content-Type: text/plain");
        writer.println("Connection: close");
        writer.println("");
        writer.println("500 - Internal Server Error");
    }
}
