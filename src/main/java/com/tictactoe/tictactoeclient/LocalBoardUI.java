package com.tictactoe.tictactoeclient;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;

public class LocalBoardUI {
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
    private LocalBoardState board;

    public void initialize() {
        board = new LocalBoardState(new char[9], 'O', 'O');
        box = new ArrayList<>(Arrays.asList(box1, box2, box3, box4, box5, box6, box7, box8, box9));
        tiles = new ArrayList<>(Arrays.asList(tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9));
        tiles.forEach(stackPane -> stackPane.setDisable(true));
        winningLine.setVisible(false);
    }

    @FXML
    void startingGame() {
        board.setPlayerToken(board.getStartingToken());
        startButton.setVisible(false);
        tiles.forEach(stackPane -> stackPane.setDisable(false));
        box.forEach(label -> label.setText(""));
        winningLine.setVisible(false);
        gameLabel.setText("Player " + board.getPlayerToken() + "'s turn");
    }

    @FXML
    public void playerAction(MouseEvent e) {
        for (int i = 0; i < 9; i++) {
            if (e.getSource().equals(tiles.get(i)) && box.get(i).getText().isEmpty()) {
                board.setMove(i);
                setPlayerSymbol(box.get(i));
            }
        }
    }

    public void setPlayerSymbol(Label label) {
        if (board.getPlayerToken() == 'X') label.setText("X");
        else label.setText("O");
        checkIfGameIsOver(board.checkBoard());
    }

    public void gameEnd(List<StackPane> winningLabels) {
        tiles.forEach(stackPane -> stackPane.setDisable(true));
        if (winningLabels != null) {
            drawWinningLine(winningLabels);
            winningLine.setVisible(true);
        }
        startButton.setVisible(true);
        board.clear();
        board.setStartingToken(board.changeToken(board.getStartingToken()));
    }

    private void drawWinningLine(List<StackPane> winningLabels) {
        winningLine.setStartX(winningLabels.get(0).getLayoutX() + 50);
        winningLine.setStartY(winningLabels.get(0).getLayoutY() + 50);
        winningLine.setEndX(winningLabels.get(2).getLayoutX() + 50);
        winningLine.setEndY(winningLabels.get(2).getLayoutY() + 50);
        winningLine.setLayoutX(winningLine.getLayoutX());
        winningLine.setLayoutY(winningLine.getLayoutY());
    }

    public void checkIfGameIsOver(String result) {
        if (Objects.equals(result, "N")) {
            board.setPlayerToken(board.changeToken(board.getPlayerToken()));
            gameLabel.setText("Player " + board.getPlayerToken() + "'s turn");
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

        if (result.charAt(0) == 'X') {
            gameLabel.setText("X won!");
            ScoreBoardX.setText("" + board.updateScore('X'));
        } else if (result.charAt(0) == 'O') {
            gameLabel.setText("O won!");
            ScoreBoardO.setText("" + board.updateScore('O'));
        } else {
            gameLabel.setText("Draw Game");
            ScoreBoardDraw.setText("" + board.updateScore('D'));
        }

        updateGameHistory(result.charAt(0));
        gameEnd(winningLabels);
    }

    public void updateGameHistory(char result) {
        gameHistory.getItems().add(board.endStateHistory(result));
    }

    public void returnToLobby() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("title-view.fxml")));
        Stage stage = (Stage) startButton.getParent().getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}