package com.tictactoe.message;

import java.io.Serializable;

public record ConnectToGame(String gameName, String userName, boolean connection, boolean VsAI) implements Serializable {}