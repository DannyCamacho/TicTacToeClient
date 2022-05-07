package com.tictactoe.message;

import java.io.Serializable;

public record UpdateGame(String gameName, String [] userTokens, char currentToken, char [] boardState, String result) implements Serializable {}