package com.tictactoe.message;

import java.io.Serializable;

public record UpdateGameHistory(String gameName, String userName, int [] xodWins, String [] gameHistory) implements Serializable {}