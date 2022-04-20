package com.tictactoe.message;

import java.io.Serializable;

public record PlayerMoveSend(String GameName, char playerToken, int move, char[] boardState) implements Serializable {}