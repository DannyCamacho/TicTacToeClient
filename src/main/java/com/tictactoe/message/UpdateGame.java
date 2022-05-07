package com.tictactoe.message;

import java.io.Serializable;
import java.util.Map;

public record UpdateGame(String gameName, String [] userNames, Map<String, Character> userTokens, char currentToken, char [] boardState, String result) implements Serializable {}