package com.tictactoe.tictactoeclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardUI {
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
//    private ArrayList<BoardState> boards;
//    BoardState board;
//    GameController controller;
//    MiniMax computerPlayer;

    public void initialize() {
//        controller = new GameController();
//        computerPlayer = new MiniMax();
//        boards = new ArrayList<>();
//        board = new BoardState();
//        register(board);
        box = new ArrayList<>(Arrays.asList(box1, box2, box3, box4, box5, box6, box7, box8, box9));
        tiles = new ArrayList<>(Arrays.asList(tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9));
        tiles.forEach(stackPane -> stackPane.setDisable(true));
        winningLine.setVisible(false);
    }

    @FXML
    void startingGame(ActionEvent event) {
//        notifyObservers(0,'C');
        //controller.setPlayer(controller.getStartingPlayer());
        startButton.setVisible(false);
        tiles.forEach(stackPane -> stackPane.setDisable(false));
        box.forEach(label -> label.setText(""));
        winningLine.setVisible(false);
//        gameLabel.setText("Player " + controller.getStartingPlayer() + "'s turn");
//        if(!controller.getTwoPlayer()) {
//            if (controller.getStartingPlayer() == 'X') setComputerMove();
//        }
//        controller.changeStartingPlayer();
    }

    @FXML
    public void playerAction(MouseEvent e) {
        for (int i = 0; i < 9; i++) {
            if(e.getSource().equals(tiles.get(i)) && box.get(i).getText().isEmpty()) {
                //notifyObservers(i, controller.getPlayer());
                setPlayerSymbol(box.get(i));
                checkIfGameIsOver();
            }
        }
    }

    public void setPlayerSymbol(Label label){
//        if (controller.getPlayer() == 'X') {
//            label.setText("X");
//            controller.changePlayer();
//        } else {
//            label.setText("O");
//            controller.changePlayer();
//            if(!controller.getTwoPlayer()) {
//                if (!controller.isGameOver(board.getBoard())) setComputerMove();
//            }
//        }
//        gameLabel.setText("Player " + controller.getPlayer() + "'s turn");
    }

    public void gameEnd(List<StackPane> winningLabels) {
        tiles.forEach(stackPane -> stackPane.setDisable(true));
        if(winningLabels != null) {
            drawWinningLine(winningLabels);
            winningLine.setVisible(true);
        }
        startButton.setVisible(true);
    }

    private void drawWinningLine(List<StackPane> winningLabels) {
        winningLine.setStartX(winningLabels.get(0).getLayoutX() + 50);
        winningLine.setStartY(winningLabels.get(0).getLayoutY() + 50);
        winningLine.setEndX(winningLabels.get(2).getLayoutX() + 50);
        winningLine.setEndY(winningLabels.get(2).getLayoutY() + 50);
        winningLine.setLayoutX(winningLine.getLayoutX());
        winningLine.setLayoutY(winningLine.getLayoutY());
    }

    public void checkIfGameIsOver(){
        for (int a = 0; a < 8; a++) {
            String line = switch (a) {
                case 0 -> box1.getText() + box2.getText() + box3.getText();
                case 1 -> box4.getText() + box5.getText() + box6.getText();
                case 2 -> box7.getText() + box8.getText() + box9.getText();
                case 3 -> box1.getText() + box5.getText() + box9.getText();
                case 4 -> box3.getText() + box5.getText() + box7.getText();
                case 5 -> box1.getText() + box4.getText() + box7.getText();
                case 6 -> box2.getText() + box5.getText() + box8.getText();
                case 7 -> box3.getText() + box6.getText() + box9.getText();
                default -> null;
            };

            List<StackPane> winningLabels = new ArrayList<>();
            switch (a) {
                case 0 -> { winningLabels.add(tile1); winningLabels.add(tile2); winningLabels.add(tile3);}
                case 1 -> { winningLabels.add(tile4); winningLabels.add(tile5); winningLabels.add(tile6);}
                case 2 -> { winningLabels.add(tile7); winningLabels.add(tile8); winningLabels.add(tile9);}
                case 3 -> { winningLabels.add(tile1); winningLabels.add(tile5); winningLabels.add(tile9);}
                case 4 -> { winningLabels.add(tile3); winningLabels.add(tile5); winningLabels.add(tile7);}
                case 5 -> { winningLabels.add(tile1); winningLabels.add(tile4); winningLabels.add(tile7);}
                case 6 -> { winningLabels.add(tile2); winningLabels.add(tile5); winningLabels.add(tile8);}
                case 7 -> { winningLabels.add(tile3); winningLabels.add(tile6); winningLabels.add(tile9);}
            }

            //X winner
            assert line != null;
            if (line.equals("XXX")) {
                gameLabel.setText("X won!");
                ScoreBoardX.setText("" + ++xWin);
                //updateGameHistory("X");
                gameEnd(winningLabels);
                a=8;
            }
            //O winner
            else if (line.equals("OOO")) {
                gameLabel.setText("O won!");
                ScoreBoardO.setText("" + ++oWin);
                //updateGameHistory("O");
                gameEnd(winningLabels);
                a=8;
            }
            // Draw
//            else if(controller.checkIfGameOver(board.getBoard()) == 'D' && a == 7) {
//                gameLabel.setText("Draw Game");
//                ScoreBoardDraw.setText("" + ++draw);
//                updateGameHistory("Draw");
//                gameEnd(null);
//            }
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
        //notifyObservers(0,'C');
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

//    @FXML
//    public void updateGameHistory(String result) {
//        gameHistory.getItems().add(board.endStateHistory(result));
//    }
//
//    public void setComputerMove() {
//        int move = computerPlayer.getMove(board.getBoard());
//        notifyObservers(move, controller.getPlayer());
//        setPlayerSymbol(box.get(move));
//    }
//
//    public void register(BoardState board) {
//        boards.add(board);
//    }
//
//    public void unregister(BoardState board) {
//        boards.remove(board);
//    }
//
//    public void notifyObservers(int move, char arg) {
//        for (BoardState board: boards) board.update(move, arg);
//    }
}
