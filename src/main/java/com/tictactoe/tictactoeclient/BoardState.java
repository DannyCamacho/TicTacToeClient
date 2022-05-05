package com.tictactoe.tictactoeclient;

public class BoardState {
    private char[] board;
    private char playerToken;
    private char currentPlayer;

    public BoardState(char [] board) {
        this.board = board;
    }

    public char[] getBoard() {
        return board;
    }

    public void setBoard(char[] board) {
        System.arraycopy(board, 0, this.board, 0, 9);
    }

    public String endStateHistory(String result) {
        String s = switch (result) {
            case "X" -> "\t\tPlayer X Win\n";
            case "O" -> "\t\tPlayer O Win\n";
            default -> "\t\tDraw Game\n";
        };

        return (board[0] == 0 ? "  " : board[0]) + "  |  " +
               (board[1] == 0 ? "  " : board[1]) + "  |  " +
               (board[2] == 0 ? "  " : board[2]) + "\n" +
               (board[3] == 0 ? "  " : board[3]) + "  |  " +
               (board[4] == 0 ? "  " : board[4]) + "  |  " +
               (board[5] == 0 ? "  " : board[5]) + s +
               (board[6] == 0 ? "  " : board[6]) + "  |  " +
               (board[7] == 0 ? "  " : board[7]) + "  |  " +
               (board[8] == 0 ? "  " : board[8]);
    }
}