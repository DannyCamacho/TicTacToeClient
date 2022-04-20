package com.tictactoe.message;

import java.io.Serializable;

public record PlayerMoveResult(String GameName, char playerToken, int move, String result) implements Serializable {}