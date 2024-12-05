package com.example.tictactoeol.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tictactoeol.Screen.HomeScreen
import com.example.tictactoeol.Screen.LobbyScreen

@Composable
fun TicTacToeNav(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") { HomeScreen(navController)}
        composable("Lobby") { LobbyScreen(navController)}
    }

}