package com.tictactoe.tictactoeclient;

import com.tictactoe.message.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Lobby {
    public AnchorPane lobby;
    private Socket socket;
    String hostname = "localhost";
    int port = 8000;
    private String userName;
    private String gameName;
    private char playerToken;
    private ObjectOutputStream output;
    public ListView<String> gameList;
    public Button connectButton, joinGameButton, refreshListButton, createGameButton;
    public Label userNameLabel;
    public TextField userNameTextField, newGameTextField;

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(new ServerConnection(userName, true));
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
            output.writeObject(new ConnectToGame(newGameTextField.getText(), userName, '\0'));
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public void update(Object message) throws IOException {
        if (message instanceof ServerConnection) {
            userNameTextField.setVisible(false);
            connectButton.setVisible(false);
            joinGameButton.setDisable(false);
            refreshListButton.setDisable(false);
            createGameButton.setDisable(false);
            newGameTextField.setDisable(false);
            gameList.setDisable(false);
            Platform.runLater(() -> userNameLabel.setText("User Name: " + userName));
            try {
                output.writeObject(new GameListRequest(userName));
            } catch (IOException ex) {
                System.out.println("I/O Error: " + ex.getMessage());
            }
        } else if (message instanceof GameListResult) {
            Platform.runLater(() -> gameList.getItems().clear());
            for (String game : ((GameListResult)message).games()) {
                Platform.runLater(() -> gameList.getItems().add(game));
            }
        } else if (message instanceof ConnectToGame) {
            playerToken = ((ConnectToGame)message).playerToken();
            gameName = ((ConnectToGame)message).gameName();
            switchToBoard();
        } else if (message instanceof PlayerMoveResult) {
            int i = ((PlayerMoveResult) message).move();
            Platform.runLater(() -> setPlayerSymbol(box.get(i)));
            currentPlayer = ((PlayerMoveResult) message).playerToken();
            checkIfGameIsOver(((PlayerMoveResult) message).result());
        }
    }

    public void switchToBoard() {
        Platform.runLater(() -> {
            lobby.setVisible(false);
            board.setVisible(true);
            initialize();
        });
    }

    // BoardUI:

    public AnchorPane board;
    @FXML
    private ListView<String> gameHistory;
    @FXML
    private Button startButton;
    @FXML
    private Label gameLabel, ScoreBoardO, ScoreBoardX, ScoreBoardDraw;
    @FXML
    private Label box1, box2, box3, box4, box5, box6, box7, box8, box9;
    @FXML
    private StackPane tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9;
    @FXML
    private Line winningLine;
    private List<StackPane> tiles;
    private List<Label> box;
    private int xWin, oWin, draw;
    private char startingPlayer = 'O';
    private char currentPlayer = 'O';

    public void initialize() {
        Platform.runLater(() -> {
        box = new ArrayList<>(Arrays.asList(box1, box2, box3, box4, box5, box6, box7, box8, box9));
        tiles = new ArrayList<>(Arrays.asList(tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9));
        tiles.forEach(stackPane -> stackPane.setDisable(true));
        winningLine.setVisible(false);
        });
    }

    @FXML
    void startingGame(ActionEvent event) {
        startButton.setVisible(false);
        tiles.forEach(stackPane -> stackPane.setDisable(false));
        box.forEach(label -> label.setText(""));
        winningLine.setVisible(false);
        String playerTurn = currentPlayer == playerToken ? "Your Turn" : "Waiting for Opponent";
        gameLabel.setText(playerTurn);
    }

    @FXML
    public void playerAction(MouseEvent e) {
        if (currentPlayer == playerToken) {
            for (int i = 0; i < 9; i++) {
                if (e.getSource().equals(tiles.get(i)) && box.get(i).getText().isEmpty()) {
                    try {
                        output.writeObject(new PlayerMoveSend(gameName, playerToken, i, createBoard()));
                        output.flush();
                    } catch (IOException ex) {
                        System.out.println("I/O Error: " + ex.getMessage());
                    }
                }
            }
        }
    }

    public void setPlayerSymbol(Label label){
        label.setText(currentPlayer + "");
        String playerTurn = currentPlayer == playerToken ? "Your Turn" : "Waiting for Opponent";
        gameLabel.setText(playerTurn);
    }

    public void gameEnd(List<StackPane> winningLabels) {
        Platform.runLater(() -> {
            tiles.forEach(stackPane -> stackPane.setDisable(true));
            if(winningLabels != null) {
                drawWinningLine(winningLabels);
                winningLine.setVisible(true);
            }
            startButton.setVisible(true);
        });
    }

    private void drawWinningLine(List<StackPane> winningLabels) {
        Platform.runLater(() -> {
            winningLine.setStartX(winningLabels.get(0).getLayoutX() + 50);
            winningLine.setStartY(winningLabels.get(0).getLayoutY() + 50);
            winningLine.setEndX(winningLabels.get(2).getLayoutX() + 50);
            winningLine.setEndY(winningLabels.get(2).getLayoutY() + 50);
            winningLine.setLayoutX(winningLine.getLayoutX());
            winningLine.setLayoutY(winningLine.getLayoutY());
        });
    }

    public void checkIfGameIsOver(String result){
        if (Objects.equals(result, "N")) return;
        List<StackPane> winningLabels = new ArrayList<>();

        switch (result.charAt(1)) {
            case '0' -> { winningLabels.add(tile1); winningLabels.add(tile2); winningLabels.add(tile3); }
            case '1' -> { winningLabels.add(tile4); winningLabels.add(tile5); winningLabels.add(tile6); }
            case '2' -> { winningLabels.add(tile7); winningLabels.add(tile8); winningLabels.add(tile9); }
            case '3' -> { winningLabels.add(tile1); winningLabels.add(tile4); winningLabels.add(tile7); }
            case '4' -> { winningLabels.add(tile2); winningLabels.add(tile5); winningLabels.add(tile8); }
            case '5' -> { winningLabels.add(tile3); winningLabels.add(tile6); winningLabels.add(tile9); }
            case '6' -> { winningLabels.add(tile1); winningLabels.add(tile5); winningLabels.add(tile9); }
            case '7' -> { winningLabels.add(tile3); winningLabels.add(tile5); winningLabels.add(tile7); }
            default -> {}
        }

        if (result.charAt(0) == 'X') {
            Platform.runLater(() -> {
                gameLabel.setText("X won!");
                ScoreBoardX.setText("" + ++xWin);
                updateGameHistory("X");
                gameEnd(winningLabels);
            });
        } else if (result.charAt(0) == 'O') {
            Platform.runLater(() -> {
                gameLabel.setText("O won!");
                ScoreBoardO.setText("" + ++oWin);
                updateGameHistory("O");
                gameEnd(winningLabels);
            });
        } else if (result.charAt(0) == 'D') {
            Platform.runLater(() -> {
                gameLabel.setText("Draw Game");
                ScoreBoardDraw.setText("" + ++draw);
                updateGameHistory("Draw");
                gameEnd(null);
            });
        }
    }

    @FXML
    public void switchMenuScene(ActionEvent event) throws IOException {
        FXMLLoader root = new FXMLLoader(ClientApplication.class.getResource("TitleScreen.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load(), 600,400);
        stage.setScene(scene);
    }

    @FXML
    public void resetBoard(ActionEvent actionEvent) {
        gameLabel.setText("Tic-Tac-Toe");
        tiles.forEach(stackPane -> stackPane.setDisable(false));
        box.forEach(label -> label.setText(""));
        winningLine.setVisible(false);
        gameEnd(null);
    }

    @FXML
    public void resetScore(ActionEvent actionEvent) {
        ScoreBoardO.setText("0");
        ScoreBoardX.setText("0");
        ScoreBoardDraw.setText("0");
        oWin = xWin = draw = 0;
        gameHistory.getItems().clear();
    }

    public char [] createBoard() {
        char [] boardState = new char[9];
        for (int i = 0; i < 9; ++i) {
            if (!box.get(i).getText().isEmpty()) {
                boardState[i] = box.get(i).getText().charAt(0);
            } else boardState[i] = '\0';
        }
        return boardState;
    }

    @FXML
    public void updateGameHistory(String result) {
        BoardState boardState = new BoardState(createBoard());
        Platform.runLater(() -> gameHistory.getItems().add(boardState.endStateHistory(result)));
    }
}