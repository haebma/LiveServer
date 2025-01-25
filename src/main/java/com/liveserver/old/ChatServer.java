package com.liveserver.old;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private static final int MAX_USERS = 10;
    private static ServerSocket serverSocket;
    private static int serverPort = 2345;
    private static ArrayList<PrintWriter> writers = new ArrayList<>();
    private static ArrayList<BufferedReader> readers = new ArrayList<>();
    private static int connectedUsers = 0;


    public static void main(String[] argv) throws IOException{
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Server auf Port " + serverPort + " gestartet. Warte auf Verbindungen...");
            while (true){
                Socket clientSocket = serverSocket.accept();

                synchronized (ChatServer.class){
                    if (connectedUsers >= MAX_USERS){
                        String message = "Maximale Benutzerzahl erreicht. Verbindung abgelehnt.\n";
                        System.out.print(message);
                        clientSocket.getOutputStream().write(message.getBytes());
                        clientSocket.close();
                        continue;
                    }
                    connectedUsers++;
                }

                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Starten des Servers: " + e.getMessage());
        }
    }

    public static synchronized void writeToAll(String message) throws IOException{
        for (PrintWriter writer: writers){
            writer.println(message);
            writer.println();
        }
    }

    public static synchronized void removeClient (PrintWriter out){
        writers.remove(out);
        connectedUsers--;
    }

    public static synchronized void addClient(PrintWriter writer){
        writers.add(writer);
    }
}
