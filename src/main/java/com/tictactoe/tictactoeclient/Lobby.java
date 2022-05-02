package com.tictactoe.tictactoeclient;

import com.tictactoe.message.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class Lobby {
    private Socket socket;
    private String hostname = "localhost";
    private int port = 8000;
    private static String userName;
    private ObjectOutputStream output;
    public ListView<String> gameList;
    public Button connectButton, joinGameButton, refreshListButton, createGameButton;
    public Label userNameLabel;
    public TextField userNameTextField, newGameTextField;

    public void initialize() {
        if (!Objects.equals(userName, null)) {
            output = ReadThread.getOutputStream();
            Platform.runLater(() -> {
                userNameTextField.setVisible(false);
                connectButton.setVisible(false);
                joinGameButton.setDisable(false);
                refreshListButton.setDisable(false);
                createGameButton.setDisable(false);
                newGameTextField.setDisable(false);
                gameList.setDisable(false);
                userNameLabel.setText("User Name: " + userName);
                onRefreshButtonPressed(null);
            });
        }
    }

    public void execute() {
        try {
            socket = new Socket(hostname, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(new ServerConnection("Player", userName, true));
            new ReadThread(socket, this).start();
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public void onConnectButtonPressed(ActionEvent actionEvent) {
        if (userNameTextField.getText().equals("")) return;
        userName = userNameTextField.getText();
        execute();
    }

    public void onRefreshButtonPressed(ActionEvent actionEvent) {
        try {
            output.writeObject(new GameListRequest(userName));
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public void onNewGameButtonPressed(ActionEvent actionEvent) {
        if (newGameTextField.getText().equals("")) return;
        try {
            output.writeObject(new ConnectToGame(newGameTextField.getText(), userName, true));
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public void update(Object message) throws IOException {
        if (message instanceof ServerConnection) {
            ReadThread.setOutputStream(output);
            userNameTextField.setVisible(false);
            connectButton.setVisible(false);
            joinGameButton.setDisable(false);
            refreshListButton.setDisable(false);
            createGameButton.setDisable(false);
            newGameTextField.setDisable(false);
            gameList.setDisable(false);
            Platform.runLater(() -> userNameLabel.setText("User Name: " + userName));
            onRefreshButtonPressed(null);
        } else if (message instanceof GameListResult) {
            Platform.runLater(() -> {
                gameList.getItems().clear();
                for (String game : ((GameListResult)message).games())
                    gameList.getItems().add(game);
            });
        }
    }
}