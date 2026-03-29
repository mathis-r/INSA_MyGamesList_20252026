package com.insa.mygameslist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.insa.mygameslist.data.IGDB
import com.insa.mygameslist.ui.theme.MyGamesListTheme


data object Home
data class Game(val nbGame: Int)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {

            MyGamesListTheme {
                NavExample()
            }
        }
    }

    @Composable
    fun NavExample() {
        val gl = GameList()
        val gi = GameInfo()

        val backStack = remember { mutableStateListOf<Any>(Home) }
        val favlist = remember { mutableStateListOf<Int>() }
        (0..<IGDB.games.size).forEach { _ ->
            favlist.add(0)
        }

        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = { key ->
                when (key) {
                    is Home -> NavEntry(key) {
                        gl.Content(backStack, favlist)
                    }

                    is Game -> NavEntry(key) {
                        gi.Content(backStack, key.nbGame, favlist)
                    }

                    else -> NavEntry(Unit) { Text("Unknown route") }
                }
            }
        )
    }

}