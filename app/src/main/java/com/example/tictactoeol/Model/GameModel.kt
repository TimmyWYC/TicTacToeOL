package com.example.tictactoeol.Model

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.tictactoeol.DataClass.Game
import com.example.tictactoeol.DataClass.Player
import com.example.tictactoeol.Enum.GameState
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow

class GameModel: ViewModel(){
    val db = Firebase.firestore
    var localPlayerId = mutableStateOf<String?>(null)
    val playerMap = MutableStateFlow<Map<String, Player>>(emptyMap())
    val gameMap = MutableStateFlow<Map<String, Game>>(emptyMap())

    /**
     * Initializes the game by setting up listeners to Firestore collections.
     * Listens for changes in players and games collections and updates local state.
     */
    fun initGame() {
        // Listen for players
        db.collection("players")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (value != null) {
                    val updatedMap = value.documents.associate { doc ->
                        doc.id to doc.toObject(Player::class.java)!!
                    }
                    playerMap.value = updatedMap
                }
            }

        // Listen for games
        db.collection("games")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (value != null) {
                    val updatedMap = value.documents.associate { doc ->
                        doc.id to doc.toObject(Game::class.java)!!
                    }
                    gameMap.value = updatedMap
                }
            }
    }

    /**
     * Checks for a winner in a given game board.
     *
     * @param board A list representing the game board.
     * @return 1 if player 1 wins, 2 if player 2 wins, 3 if the game is a draw, or 0 if no winner yet.
     */
    fun checkForWinner(board: List<Int>): Int{
        if (board[0] != 0 && board[0] == board[1] && board[0] == board[2]){
            return board[0]
        }
        if (board[3] != 0 && board[3] == board[4] && board[3] == board[5]){
            return board[3]
        }
        if (board[6] != 0 && board[6] == board[7] && board[6] == board[8]){
            return board[6]
        }


        if (board[0] != 0 && board[0] == board[3] && board[0] == board[6]){
            return board[0]
        }
        if (board[1] != 0 && board[1] == board[4] && board[1] == board[7]){
            return board[1]
        }
        if (board[2] != 0 && board[2] == board[5] && board[2] == board[8]){
            return board[2]
        }


        if (board[0] != 0 && board[0] == board[4] && board[0] == board[8]){
            return board[0]
        }
        if (board[2] != 0 && board[2] == board[4] && board[2] == board[6]){
            return board[2]
        }

        if (!board.contains(0)){
            return 3
        }

        return 0
    }

    /**
     * Checks the current state of a game and updates the game board and state if necessary.
     *
     * @param gameId The ID of the game to check.
     * @param index The index of the board that the player is trying to mark.
     */
    fun checkGameState(gameId: String?, index: Int){
        if (gameId != null) {
            val game: Game? = gameMap.value[gameId]

            if (game != null) {

                val myTurn = game.gameState == GameState.player1_turn && game.player1Id == localPlayerId.value || game.gameState == GameState.player2_turn && game.player2Id == localPlayerId.value
                val board: MutableList<Int> = game.gameBoard.toMutableList()
                var currentGameState = ""

                if (!myTurn) return

                if (game.gameState == GameState.player1_turn) {
                    board[index] = 1
                    currentGameState = GameState.player2_turn.toString()
                } else if (game.gameState == GameState.player2_turn) {
                    board[index] = 2
                    currentGameState = GameState.player1_turn.toString()
                }

                val winner = checkForWinner(board.toList())
                if (winner == 1) {
                    currentGameState = GameState.player1_won.toString()
                } else if (winner == 2) {
                    currentGameState = GameState.player2_won.toString()
                } else if (winner == 3) {
                    currentGameState = GameState.draw.toString()
                }

                db.collection("games").document(gameId)
                    .update(
                        "gameBoard", board,
                        "gameState", currentGameState
                    )
            }
        }
    }

}