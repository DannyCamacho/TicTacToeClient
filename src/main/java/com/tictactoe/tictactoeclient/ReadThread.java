package com.tictactoe.tictactoeclient;

import com.tictactoe.message.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
                if (message instanceof ServerConnection || message instanceof GameListResult) {
                    lobby.update(message);
                } else if (message instanceof UpdateGame) {
                    if (Objects.equals(((UpdateGame) message).result(), "Connect to Game")) {
                        Platform.runLater(() -> {
                            FXMLLoader root = new FXMLLoader(ClientApplication.class.getResource("board-view.fxml"));
                            Stage stage = (Stage) lobby.connectButton.getParent().getScene().getWindow();
                            Scene scene = null;
                            try {
                                scene = new Scene(root.load(), 600, 400);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            stage.setScene(scene);
                        });
                        board.update(message);
                    } else {
                        board.update(message);
                    }
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