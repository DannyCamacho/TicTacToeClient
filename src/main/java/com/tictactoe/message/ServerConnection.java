package com.tictactoe.message;

import java.io.Serializable;

public record ServerConnection(String connectType, String userName, boolean connection) implements Serializable {}
// true is connect, false is disconnect.