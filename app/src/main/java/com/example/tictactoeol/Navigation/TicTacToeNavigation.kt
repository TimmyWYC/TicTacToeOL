package com.example.tictactoeol.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tictactoeol.Model.GameModel
import com.example.tictactoeol.Screen.GameScreen
import com.example.tictactoeol.Screen.HomeScreen
import com.example.tictactoeol.Screen.LobbyScreen

/**
 * Composable function that sets up the navigation for the Tic-Tac-Toe game.
 *
 * Initializes the navigation controller and the game model, then defines the navigation structure
 * for the app using [NavHost]. The app includes three screens:
 * - **Home**: The home screen where the user can start a new game or join an existing one.
 * - **Lobby**: The lobby screen for managing the game session.
 * - **Game**: The game screen where the actual Tic-Tac-Toe game is played.
 */
@Composable
fun TicTacToeNav(){
    val navController = rememberNavController()
    val model = GameModel()
    model.initGame()

    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") {
            HomeScreen(navController, model)
        }
        composable("Lobby") {
            LobbyScreen(navController, model)
        }
        composable("game/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")
            GameScreen(navController, model, gameId)
        }
    }

}