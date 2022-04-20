package com.tictactoe.message;

import java.io.Serializable;

public record Message(String messageType, String message, char[] boardState) implements Serializable {}

