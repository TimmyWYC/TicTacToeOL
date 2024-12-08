package com.example.tictactoeol.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tictactoeol.Enum.GameState
import com.example.tictactoeol.Model.GameModel
import com.example.tictactoeol.R
import kotlinx.coroutines.flow.asStateFlow

/**
 * Composable function that displays the game screen for Tic-Tac-Toe.
 *
 * This screen shows the game board, the current player's turn, and the result of the game (if any).
 *
 * @param navController The navigation controller used to navigate between screens.
 * @param model The game model that provides game state and player data.
 * @param gameId The ID of the game being played.
 */
@Composable
fun GameScreen(navController: NavController, model: GameModel, gameId: String?) {
    val players by model.playerMap.asStateFlow().collectAsStateWithLifecycle()
    val games by model.gameMap.asStateFlow().collectAsStateWithLifecycle()
    val game = games[gameId]

    Scaffold { innerPadding ->
        Image(
            painter = painterResource(id = R.drawable.bgi), // Replace with your image resource
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(innerPadding).fillMaxWidth()
        ) {
            when (game?.gameState.toString()) {
                GameState.player1_won.toString(), GameState.player2_won.toString(), GameState.draw.toString() -> {
                    if (game?.gameState == GameState.draw) {
                        Text("Draw!")
                    } else if (game?.gameState == GameState.player1_won) {
                        Text("${players[game.player1Id]!!.name} won!")
                    } else {
                        Text("${players[game?.player2Id]!!.name} won!")
                    }
                    Button(onClick = {
                        navController.navigate("lobby")
                    }) {
                        Text("Go to lobby")
                    }
                }

                else -> {
                    val isItMyTurn =
                        game?.gameState == GameState.player1_turn && game.player1Id == model.localPlayerId.value
                                || game?.gameState == GameState.player2_turn && game.player2Id == model.localPlayerId.value
                    val turn =
                        if (isItMyTurn) "${players[game?.player1Id]!!.name}'s turn" else "${players[game?.player2Id]!!.name}'s turn"
                    Text(turn)
                }
            }

            for (i in 0..<3) {
                Row {
                    for (j in 0..<3) {
                        Button(
                            shape = RectangleShape,
                            modifier = Modifier.size(100.dp).padding(5.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            onClick = {
                                model.checkGameState(gameId, i * 3 + j)
                            }
                        ) {
                            if (game != null) {
                                if (game.gameBoard[i * 3 + j] == 1) {
                                    Text(
                                        text = "x",
                                        fontSize = 60.sp
                                    )
                                } else if (game.gameBoard[i * 3 + j] == 2) {
                                    Text(
                                        text = "O",
                                        fontSize = 60.sp
                                    )
                                } else {
                                    Text("")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}