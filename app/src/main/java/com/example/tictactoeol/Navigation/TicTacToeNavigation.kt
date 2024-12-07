package com.example.tictactoeol.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tictactoeol.Model.GameModel
import com.example.tictactoeol.Screen.HomeScreen
import com.example.tictactoeol.Screen.LobbyScreen

@Composable
fun TicTacToeNav(){
    val navController = rememberNavController()
    val model = GameModel()
    model.initGame()

    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") { HomeScreen(navController, model)}
        composable("Lobby") { LobbyScreen(navController, model)}
    }

}