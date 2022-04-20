package com.tictactoe.tictactoeclient;

import java.io.*;
import java.net.*;

public class ReadThread extends Thread {
    private ObjectInputStream fromServer;
    private final Lobby lobby;

    public ReadThread(Socket socket, Lobby lobby) {
        this.lobby = lobby;

        try {
            fromServer = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
           System.out.println("\nError getting input stream: " + ex.getMessage() + "\n");
        }
    }

    public void run() {
        while (true) {
            try {
                lobby.update(fromServer.readObject());
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("\nError reading from server: " + ex.getMessage()+ "\n");
                break;
            }
        }
    }
}