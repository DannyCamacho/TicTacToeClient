package com.tictactoe.message;

import java.io.Serializable;
import java.util.Set;

public record GameListResult(Set<String> games) implements Serializable {}