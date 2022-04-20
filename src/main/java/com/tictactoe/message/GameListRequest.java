package com.tictactoe.message;


import java.io.Serializable;

public record GameListRequest(String userName) implements Serializable {}