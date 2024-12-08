package com.example.tictactoeol.Screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tictactoeol.DataClass.Player
import com.example.tictactoeol.Model.GameModel
import com.example.tictactoeol.R

/**
 * Composable function that displays the home screen for the Tic-Tac-Toe app.
 *
 * This screen allows the user to enter their player name and then proceed to the lobby if they have already registered or just created a new account.
 * The app checks if the user has an existing player ID stored in shared preferences.
 * If a player ID is found, they are automatically navigated to the lobby.
 *
 * @param navController The navigation controller used to navigate between screens.
 * @param model The game model that provides game state and player data.
 */
@Composable
fun HomeScreen(navController: NavController, model: GameModel){
    // sharedPreferences can be used to store and retrieve small app data, like user preferences.
    val sharedPreferences = LocalContext.current
        .getSharedPreferences("TicTacToePrefs", Context.MODE_PRIVATE)
    var playerName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        model.localPlayerId.value = sharedPreferences.getString("playerId", null)
        if (model.localPlayerId.value != null) {
            navController.navigate("Lobby")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bgi), // Replace with your image resource
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TicTacToeOnline",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(200.dp))

            TextField(
                value = playerName,
                onValueChange = { playerName = it },
                label = { Text(
                    text = "Enter your name",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                    ) },
                modifier = Modifier
                    .background(color = Color.Black)
            )

            Button(
                onClick = { if(playerName.isNotBlank()){
                    // Create new player in Firestore
                    val newPlayer = Player(name = playerName)
                    model.db.collection("players")
                        .add(newPlayer)
                        .addOnSuccessListener { documentRef ->
                            val newPlayerId = documentRef.id

                            // Save and update player info
                            sharedPreferences.edit().putString("playerId", newPlayerId).apply()
                            model.localPlayerId.value = newPlayerId
                            navController.navigate("Lobby")
                        }
                } },
                shape = RectangleShape,

            ) {
                Text("Start")
            }

            Spacer(modifier = Modifier.height(300.dp))
        }
    }
}