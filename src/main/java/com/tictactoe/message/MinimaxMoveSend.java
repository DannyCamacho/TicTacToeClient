package com.tictactoe.message;

import java.io.Serializable;

public record MinimaxMoveSend(String gameName, char playerToken, int move, char[] boardState) implements Serializable {}