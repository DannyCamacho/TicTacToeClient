package com.tictactoe.message;

import java.io.Serializable;
import java.util.Map;

public record UpdateGame(String gameName, Map<String, Character> users, char currentToken, char [] boardState, String result) implements Serializable {}