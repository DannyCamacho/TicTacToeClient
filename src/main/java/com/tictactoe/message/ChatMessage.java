package com.tictactoe.message;

import java.io.Serializable;

public record ChatMessage(String gameName, String userName, String message) implements Serializable {}