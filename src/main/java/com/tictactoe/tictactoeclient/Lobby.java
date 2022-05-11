package com.tictactoe.tictactoeclient;

import com.tictactoe.message.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class Lobby {
    private Socket socket;
    private String hostname = "192.168.1.218";
    private int port = 8000;
    private static String userName;
    private static boolean connected = false;
    private ObjectOutputStream output;
    public ListView<String> gameList;
    public Button connectButton, joinGameButton, refreshListButton, createGameButton, mainMenuButton, vsAIButton, sendButton;
    public Label userNameLabel;
    public TextField userNameTextField, newGameTextField, chatTextField;
    public TextArea ta;

    public void initialize() {
        if (connected) {
            ReadThread.setLobby(this);
            output = ReadThread.getOutputStream();
            Platform.runLater(() -> {
                userNameTextField.setVisible(false);
                connectButton.setVisible(false);
                joinGameButton.setDisable(false);
                refreshListButton.setDisable(false);
                createGameButton.setDisable(false);
                newGameTextField.setDisable(false);
                vsAIButton.setDisable(false);
                gameList.setDisable(false);
                sendButton.setDisable(false);
                chatTextField.setDisable(false);
                ta.setDisable(false);
                userNameLabel.setText("User Name: " + userName);
                ta.appendText("Welcome to the Tic-Tac-Toe Lobby!\n");
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
            output.writeObject(new ConnectToGame(newGameTextField.getText(), userName, true, false));
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public void onJoinGamePressed() {
        if (Objects.equals(gameList.getSelectionModel().getSelectedItem(), "")) return;
        try {
            output.writeObject(new ConnectToGame(gameList.getSelectionModel().getSelectedItem(), userName, true, false));
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public void onMainMenuButtonPressed() throws IOException {
        if (connected) {
            output.writeObject(new ServerConnection("Player", userName, false));
            userName = "";
            connected = false;
        }
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("title-view.fxml")));
        Stage stage = (Stage)connectButton.getParent().getScene().getWindow();
        Scene scene = new Scene(root);
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.show();
        });
    }

    public void onVsAIButtonPressed() {
        try {
            output.writeObject(new ConnectToGame(userName + " vs AI Player", userName, true, true));
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public void update(Object message) throws IOException {
        if (message instanceof ServerConnection) {
            if (((ServerConnection)message).connection()) {
                connected = true;
                ReadThread.setOutputStream(output);
                userNameTextField.setVisible(false);
                connectButton.setVisible(false);
                joinGameButton.setDisable(false);
                refreshListButton.setDisable(false);
                createGameButton.setDisable(false);
                newGameTextField.setDisable(false);
                vsAIButton.setDisable(false);
                gameList.setDisable(false);
                sendButton.setDisable(false);
                chatTextField.setDisable(false);
                ta.setDisable(false);
                Platform.runLater(() -> {
                    userNameLabel.setText("User Name: " + userName);
                    ta.appendText("Welcome to the Tic-Tac-Toe Lobby!\n");
                });
                onRefreshButtonPressed();
            }
        } else if (message instanceof GameListResult) {
            Platform.runLater(() -> gameList.getItems().clear());
            for (String game : ((GameListResult) message).games()) {
                Platform.runLater(() -> gameList.getItems().add(game));
            }
        } else if (message instanceof ConnectToGame) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("board-view.fxml")));
            Stage stage = (Stage)connectButton.getParent().getScene().getWindow();
            Scene scene = new Scene(root);
            Platform.runLater(() -> {
                stage.setScene(scene);
                stage.show();
            });
        } else if (message instanceof ChatMessage) {
            Platform.runLater(() -> ta.appendText(((ChatMessage)message).message()));
        }
    }

    public void onSendButtonClicked() throws IOException {
        if (Objects.equals(chatTextField.getText(), "")) return;
        String message = "\n" + userName + ": " + chatTextField.getText();
        output.writeObject(new ChatMessage("Lobby", null, userName, message));
        output.flush();
        chatTextField.setText("");
    }

    public void onChatKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().getCode() == 10) {
            onSendButtonClicked();
        }
    }

    public static String getUserName() {
        return userName;
    }
}