package com.tictactoe.message;

import java.io.Serializable;

public record PlayerMoveResult(String gameName, char playerToken, int move, String result, char [] board) implements Serializable {}