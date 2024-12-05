package com.example.tictactoeol.DataClass

import com.example.tictactoeol.Enum.GameState

data class Game(
    var gameBoard: List<Int> = List(9) { 0 },
    var gameState: GameState = GameState.Challange,
    var player1Id: String = "",
    var player2Id: String = ""
)
