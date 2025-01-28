/*
package com.liveserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WebClientHandler extends Thread{

    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;
    private String clientName;

    public WebClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run(){
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            writer.println("Willkommen im Chat! Wenn du wieder gehen willst, schreibe einfach 'exit'");
            writer.println("Bitte gib deinen Namen ein:");
            clientName = reader.readLine();

            synchronized (WebChatServer.class){
                WebChatServer.addClient(writer);
            }

            WebChatServer.writeToAll(clientName + " hat den Chat betreten.");

            String message;
            while ((message = reader.readLine()) != null){
                if (message.equalsIgnoreCase("exit")){
                    break;
                }
                WebChatServer.writeToAll(clientName + ": " + message);
            }

        } catch (IOException e){
            System.err.println("Fehler beim Kommunizieren mit dem Client: " + e.getMessage());
        } finally {
            try {
                WebChatServer.removeClient(writer);
                WebChatServer.writeToAll(clientName + " hat den Chat verlassen.");
                clientSocket.close();
            } catch (IOException e){
                System.err.println("Fehler beim Schließen der Verbindung " + e.getMessage());
            }
        }
    }
}
*/
