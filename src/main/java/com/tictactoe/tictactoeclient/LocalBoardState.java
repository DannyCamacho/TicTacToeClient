package com.tictactoe.tictactoeclient;

class LocalBoardState {
    private final char[] board;
    private char playerToken;
    private char startingToken;
    private int xWin, oWin, draw;

    public LocalBoardState(char [] board, char playerToken, char startingToken) {
        this.board = board;
        this.playerToken = playerToken;
        this.startingToken = startingToken;
        xWin = oWin = draw = 0;
    }

    public void setPlayerToken(char playerToken) {
        this.playerToken = playerToken;
    }

    public void setStartingToken(char startingToken) {
        this.startingToken = startingToken;
    }

    public char getPlayerToken() {
        return playerToken;
    }

    public char getStartingToken() {
        return startingToken;
    }

    public String endStateHistory(char result) {
        String s = switch (result) {
            case 'X' -> "\t\tPlayer X Win\n";
            case 'O' -> "\t\tPlayer O Win\n";
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

    public void clear() {
        for (int i = 0; i < 9; ++i) board[i] = '\0';
    }

    public void setMove(int move) {
        board[move] = playerToken;
    }

    public char changeToken(char token) {
        return token == 'X' ? 'O' : 'X';
    }

    public int updateScore(char result) {
        if (result == 'X') return ++xWin;
        else if (result == 'O') return ++oWin;
        else return ++draw;
    }

    public boolean isFullBoard() {
        return !(board[0] == 0) && !(board[1] == 0) && !(board[2] == 0) &&
                !(board[3] == 0) && !(board[4] == 0) && !(board[5] == 0) &&
                !(board[6] == 0) && !(board[7] == 0) && !(board[8] == 0);
    }

    public String checkBoard() {
        for (int i = 0; i < 3; ++i) {
            if (board[3 * i] == board[3 * i + 1] && board[3 * i] == board[3 * i + 2]) {
                if (board[3 * i] == 'X') return "X" + i;
                else if (board[3 * i] == 'O') return "O" + i;
            }
            if (board[i] == board[3 + i] && board[i] == board[6 + i]) {
                if (board[i] == 'X') return "X" + (i + 3);
                else if (board[i] == 'O') return "O" + (i + 3);
            }
        }
        if (board[0] == board[4] && board[0] == board[8]) {
            if (board[0] == 'X') return "X6";
            else if (board[0] == 'O') return "O6";
        }
        if (board[6] == board[4] && board[6] == board[2]) {
            if (board[6] == 'X') return "X7";
            else if (board[6] == 'O') return "O7";
        }
        if (isFullBoard()) return "D8";
        else return "N";
    }
}