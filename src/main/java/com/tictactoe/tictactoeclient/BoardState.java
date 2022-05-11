package com.tictactoe.tictactoeclient;

public class BoardState {
    private char[] board;
    private char playerToken;
    private char currentToken;

    public BoardState() {
        board = new char[9];
        playerToken = 0;
        currentToken = 0;
    }

    public void setBoard(char[] board) {
        this.board = board;
    }

    public void setPlayerToken(char playerToken) {
        this.playerToken = playerToken;
    }

    public void setCurrentToken(char currentPlayer) {
        this.currentToken = currentPlayer;
    }

    public char[] getBoard() {
        return board;
    }

    public char getPlayerToken() {
        return playerToken;
    }

    public char getCurrentToken() {
        return currentToken;
    }

    public boolean isPlayerTurn() {
        return playerToken == currentToken;
    }
}