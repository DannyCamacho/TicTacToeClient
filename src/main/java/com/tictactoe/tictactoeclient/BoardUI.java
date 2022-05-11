package com.tictactoe.tictactoeclient;

import com.tictactoe.message.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class BoardUI {
    public TextArea ta;
    public TextField chatTextField;
    private ObjectOutputStream output;
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
    private BoardState board;
    private String gameName, userName;

    public void initialize() {
        ReadThread.setBoard(this);
        output = ReadThread.getOutputStream();
        userName = Lobby.getUserName();
        board = new BoardState();
        Platform.runLater(() -> {
            box = new ArrayList<>(Arrays.asList(box1, box2, box3, box4, box5, box6, box7, box8, box9));
            tiles = new ArrayList<>(Arrays.asList(tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9));
            tiles.forEach(stackPane -> stackPane.setDisable(true));
            winningLine.setVisible(false);
        });
    }

    public void update(Object message) throws IOException {
        if (message instanceof UpdateGame) {
            board.setBoard(((UpdateGame)message).boardState());
            if (Objects.equals(((UpdateGame) message).result(), "Initialize")) {
                gameName = ((UpdateGame) message).gameName();
                board.setPlayerToken(((UpdateGame) message).currentToken());
                if (board.getPlayerToken() == 'S') startButton.setVisible(false);
                updateBoardUI();
                Platform.runLater(() -> gameLabel.setText("Tic-Tac-Toe"));
            } else if (Objects.equals(((UpdateGame) message).result(), "End")) {
                board.setBoard(((UpdateGame)message).boardState());
                board.setCurrentToken(((UpdateGame)message).currentToken());
                if (board.getPlayerToken() != 'S') startButton.setVisible(true);
            } else {
                board.setBoard(((UpdateGame)message).boardState());
                board.setCurrentToken(((UpdateGame)message).currentToken());
                updateBoardUI();
                checkIfGameIsOver(((UpdateGame) message).result());
            }
        } else if (message instanceof ChatMessage) {
            Platform.runLater(() -> ta.appendText(((ChatMessage) message).message()));
        } else if (message instanceof UpdateGameHistory) {
            Platform.runLater(() -> {
                ScoreBoardX.setText(((UpdateGameHistory) message).xodWins()[0]);
                ScoreBoardO.setText(((UpdateGameHistory) message).xodWins()[1]);
                ScoreBoardDraw.setText(((UpdateGameHistory) message).xodWins()[2]);
                gameHistory.getItems().clear();
            });
            for (String history : ((UpdateGameHistory) message).gameHistory()) {
                Platform.runLater(() -> gameHistory.getItems().add(history));
            }
        }
    }

    @FXML
    void startingGame() throws IOException {
        startButton.setVisible(false);
        tiles.forEach(stackPane -> stackPane.setDisable(false));
        winningLine.setVisible(false);
        output.writeObject(new UpdateGame(gameName, null, '\0', null, "New"));
        output.flush();
    }

    @FXML
    public void playerAction(MouseEvent e) {
        if (board.isPlayerTurn()) {
            for (int i = 0; i < 9; i++) {
                if (e.getSource().equals(tiles.get(i)) && box.get(i).getText().isEmpty()) {
                    try {
                        output.writeObject(new PlayerMoveSend(gameName, board.getPlayerToken(), i, board.getBoard()));
                        output.flush();
                    } catch (IOException ex) {
                        System.out.println("I/O Error: " + ex.getMessage());
                    }
                }
            }
        }
    }

    public void updateBoardUI() {
        Platform.runLater(() -> {
            for (int i = 0; i < 9; ++i) {
                if (board.getBoard()[i] == 'X') {
                    box.get(i).setText("X");
                } else if (board.getBoard()[i] == 'O') {
                    box.get(i).setText("O");
                } else {
                    box.get(i).setText("");
                }
            }
            if (!startButton.isVisible()) gameLabel.setText(board.isPlayerTurn() ? "Your Turn" : "Waiting for Opponent");
            if (board.getPlayerToken() == 'S') gameLabel.setText(board.getCurrentToken() == 'X' ? "Player X Turn" : "Player O Turn");
        });
    }

    public void gameEnd(List<StackPane> winningLabels) throws IOException {
        Platform.runLater(() -> {
            tiles.forEach(stackPane -> stackPane.setDisable(true));
            if(winningLabels != null) {
                drawWinningLine(winningLabels);
                winningLine.setVisible(true);
            }
        });
        output.writeObject(new UpdateGame(gameName, null, '\0', null, "End"));
        output.flush();
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

    public void checkIfGameIsOver(String result) {
        if (Objects.equals(result, "N")) {
            return;
        }

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
            default -> winningLabels = null;
        }

        String labelString = switch (result.charAt(0)) {
            case 'X' -> "X won!";
            case 'O' -> "O won!";
            default -> "Draw Game!";
        };

        List<StackPane> finalWinningLabels = winningLabels;
        Platform.runLater(() -> {
                gameLabel.setText(labelString);
                try {
                    gameEnd(finalWinningLabels);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        });
    }

    public void onSendButtonClicked() throws IOException {
        if (Objects.equals(chatTextField.getText(), "")) return;
        String message = userName + " [" + board.getPlayerToken() + "]: " + chatTextField.getText() + "\n";
        output.writeObject(new ChatMessage("Board", gameName, userName, message));
        output.flush();
        chatTextField.setText("");
    }

    public void onChatKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().getCode() == 10) {
            onSendButtonClicked();
        }
    }

    public void returnToLobby() throws IOException {
        output.writeObject(new ConnectToGame(gameName, userName, false, false));
        output.flush();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("lobby-view.fxml")));
        Stage stage = (Stage)startButton.getParent().getScene().getWindow();
        Scene scene = new Scene(root);
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.show();
        });
    }
}
