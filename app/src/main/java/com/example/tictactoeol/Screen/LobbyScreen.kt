package com.example.tictactoeol.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tictactoeol.DataClass.Game
import com.example.tictactoeol.Enum.GameState
import com.example.tictactoeol.Model.GameModel
import com.example.tictactoeol.R
import kotlinx.coroutines.flow.asStateFlow


/**
 * Composable function that displays the lobby screen for the Tic-Tac-Toe app.
 *
 * The lobby screen shows the list of players currently registered in the game.
 * It allows the current player to challenge another player to a game, accept challenges from other players,
 * and automatically navigates to the game screen when a game starts or when the player accepts a challenge.
 *
 * @param navController The navigation controller used to navigate between screens.
 * @param model The game model that provides the current game state, player data, and methods for interacting with Firestore.
 */
@Composable
fun LobbyScreen(navController: NavController, model: GameModel) {
    val players by model.playerMap.asStateFlow().collectAsStateWithLifecycle()
    val games by model.gameMap.asStateFlow().collectAsStateWithLifecycle()
    var hasGame = false

    // look for if there is a games /
    LaunchedEffect(games) {
        games.forEach { (gameId, game) ->
            if ((game.player1Id == model.localPlayerId.value || game.player2Id == model.localPlayerId.value) && (game.gameState == GameState.player1_turn || game.gameState == GameState.player2_turn)) {
                navController.navigate("game/${gameId}")
            }
        }
    }

    Scaffold{ innerPadding ->
        Image(
            painter = painterResource(id = R.drawable.bgi), // Replace with your image resource
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(players.entries.toList()) { (documentId, player) ->
                if (documentId != model.localPlayerId.value) { // Don't show yourself
                    ListItem(
                        headlineContent = {
                            Text("Player: ${player.name}")
                        },
                        trailingContent = {
                            games.forEach { (gameId, game) ->
                                if (game.player1Id == model.localPlayerId.value && game.gameState == GameState.Challange && documentId == game.player2Id ) {
                                    Text("Sent...")
                                    hasGame = true
                                } else if (game.player2Id == model.localPlayerId.value && game.gameState == GameState.Challange && documentId == game.player1Id ) {
                                    Button(onClick = {
                                        model.db.collection("games").document(gameId)
                                            .update("gameState", GameState.player1_turn.toString())
                                            .addOnSuccessListener {
                                                navController.navigate("game/${gameId}")
                                            }
                                    }) {
                                        Text("Accept")
                                    }
                                    hasGame = true
                                }
                            }

                            // button for challenge other player
                            if (!hasGame) {
                                Button(onClick = {
                                    model.db.collection("games")
                                        .add(
                                            Game(gameState = GameState.Challange,
                                                player1Id = model.localPlayerId.value!!,
                                                player2Id = documentId)
                                        )
                                }) {
                                    Text("Challenge")
                                }
                            }

                        }
                    )
                }
            }
        }
    }
}
