package com.tictactoe.message;

import java.io.Serializable;

public record GameControllerConnection(String userName, boolean connection) implements Serializable {}
// true is connect to server
// false is disconnect from server