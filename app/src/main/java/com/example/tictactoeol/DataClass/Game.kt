package com.example.tictactoeol.DataClass

import com.example.tictactoeol.Enum.GameState

/**
 * Represents the state of a game.
 *
 * @property gameBoard A list of 9 integers representing the game board. Default is a list of zeros.
 * @property gameState The current state of the game, defaulting to [GameState.Challange].
 * @property player1Id The ID of player 1, default is an empty string.
 * @property player2Id The ID of player 2, default is an empty string.
 */

data class Game(
    var gameBoard: List<Int> = List(9) { 0 },
    var gameState: GameState = GameState.Challange,
    var player1Id: String = "",
    var player2Id: String = ""
)
