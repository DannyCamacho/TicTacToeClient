package com.tictactoe.tictactoeclient;

import com.tictactoe.message.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    private Parent root;
    private Scene scene;
    private Stage stage;

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
                onRefreshButtonPressed();
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

    public void onConnectButtonPressed() {
        if (userNameTextField.getText().equals("")) return;
        userName = userNameTextField.getText();
        execute();
    }

    public void onRefreshButtonPressed() {
        try {
            output.writeObject(new GameListRequest(userName));
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public void onNewGameButtonPressed() {
        if (newGameTextField.getText().equals("")) return;
        try {
            output.writeObject(new ConnectToGame(newGameTextField.getText(), userName, true));
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public void onJoinGamePressed() {
        if (Objects.equals(gameList.getSelectionModel().getSelectedItem(), "")) return;
        try {
            output.writeObject(new ConnectToGame(gameList.getSelectionModel().getSelectedItem(), userName, true));
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public void update(Object message) throws IOException {
        if (message instanceof ServerConnection) {
            if (Objects.equals(((ServerConnection)message).connectType(), "Connected")) {
                ReadThread.setOutputStream(output);
                userNameTextField.setVisible(false);
                connectButton.setVisible(false);
                joinGameButton.setDisable(false);
                refreshListButton.setDisable(false);
                createGameButton.setDisable(false);
                newGameTextField.setDisable(false);
                gameList.setDisable(false);
                Platform.runLater(() -> userNameLabel.setText("User Name: " + userName));
                onRefreshButtonPressed();
            }
        } else if (message instanceof GameListResult) {
            Platform.runLater(() -> gameList.getItems().clear());
            for (String game : ((GameListResult)message).games()) {
                System.out.println(game);
                Platform.runLater(() -> gameList.getItems().add(game));
            }
        } else if (message instanceof ConnectToGame) {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("board-view.fxml")));
            stage = (Stage)connectButton.getParent().getScene().getWindow();
            scene = new Scene(root);
            Platform.runLater(() -> {
                stage.setScene(scene);
                stage.show();
            });
        }
    }

    public static String getUserName() {
        return userName;
    }
}