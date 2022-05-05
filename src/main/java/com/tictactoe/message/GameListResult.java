package com.tictactoe.message;

import java.io.Serializable;

public record GameListResult(String userName, String [] games) implements Serializable {}