package com.tictactoe.tictactoeclient;

import com.tictactoe.message.*;
import java.io.*;
import java.net.*;
import java.util.Objects;

public class ReadThread extends Thread {
    private ObjectInputStream fromServer;
    private static ObjectOutputStream output;
    private static Lobby lobby;
    private static BoardUI board;

    public ReadThread(Socket socket, Lobby newLobby) {
        try {
            lobby = newLobby;
            fromServer = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
           System.out.println("\nError getting input stream: " + ex.getMessage() + "\n");
        }
    }

    public void run() {
        while (true) {
            try {
                Object message = fromServer.readObject();
                if (message instanceof UpdateGame) {
                    board.update(message);
                } else if (message instanceof ChatMessage) {
                    if (Objects.equals(((ChatMessage) message).messageType(), "Lobby")) {
                        lobby.update(message);
                    } else {
                        board.update(message);
                    }
                } else if (message instanceof ServerConnection && !((ServerConnection) message).connection()) {
                    break;
                } else {
                    lobby.update(message);
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("\nError reading from server: " + ex.getMessage()+ "\n");
                break;
            }
        }
    }

    public static void setLobby(Lobby newLobby) {
        lobby = newLobby;
    }

    public static void setBoard(BoardUI newBoard) {
        board = newBoard;
    }

    public static void setOutputStream(ObjectOutputStream newOutput) {
        output = newOutput;
    }

    public static ObjectOutputStream getOutputStream() {
        return output;
    }
}