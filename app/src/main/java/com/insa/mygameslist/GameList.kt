package com.insa.mygameslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.insa.mygameslist.data.Games
import com.insa.mygameslist.data.IGDB

class GameList {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Content(backStack: SnapshotStateList<Any>, favList: SnapshotStateList<Int>) {
        val textFieldState = rememberTextFieldState()
        val games = IGDB.games

        // Filter items based on the current search text
        val filteredItems by remember {
            derivedStateOf {
                val searchText = textFieldState.text.toString()
                if (searchText.isEmpty()) {
                    games
                } else {
                    games.filter { game ->
                        game.name.contains(
                            searchText,
                            ignoreCase = true
                        ) || try {
                            game.genres.contains(IGDB.genres.first {
                                it.name.contains(
                                    searchText,
                                    ignoreCase = true
                                )
                            }.id)
                        } catch (_: NoSuchElementException) {
                            false
                        } || try {
                            game.platforms.contains(IGDB.platforms.first {
                                it.name.contains(
                                    searchText,
                                    ignoreCase = true
                                )
                            }.id)
                        } catch (_: NoSuchElementException) {
                            false
                        }
                    }
                }
            }
        }
        val sb = SearchBar()
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = topAppBarColors(),
                    title = {
                        sb.SimpleSearchBar(textFieldState, onSearch = {})
                    }
                )
            },
            contentWindowInsets = WindowInsets.systemBars,
        ) { innerPadding ->
            ListGame(innerPadding, backStack, filteredItems, favList)
        }
    }

    @Composable
    fun ListGame(
        innerPadding: PaddingValues,
        backStack: SnapshotStateList<Any>,
        listGame: List<Games>,
        favList: SnapshotStateList<Int>
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
        ) {
            for (game in listGame) {
                item {
                    GameBox(game, backStack, favList)
                }
            }
            if (listGame.isEmpty()) {
                item {
                    Text(
                        "No match :(", fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    @Composable
    fun GameBox(game: Games, backStack: SnapshotStateList<Any>, favList: SnapshotStateList<Int>) {
        val indices = game.genres
        val gameIndex = IGDB.games.indexOf(game)
        var genresStr = "Genres : "
        for (i in 0..<indices.size) {
            try {
                genresStr += IGDB.genres.first { it.id == indices[i] }.name
                genresStr += ", "
            } catch (_: NoSuchElementException) {
            }
        }

        val index = game.cover
        val coverUrl = "https:" + IGDB.covers.first { it.id == index }.url

        Row(
            modifier = Modifier
                .padding(12.dp)
                .clickable(onClick = {
                    backStack.add(Game(gameIndex))
                })
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            AsyncImage(model = coverUrl, contentDescription = "Cover")
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    game.name, style = TextStyle.Default.copy(
                        lineBreak = LineBreak.Simple
                    )
                )
                Text(
                    genresStr, style = TextStyle.Default.copy(
                        lineBreak = LineBreak.Simple
                    )
                )
            }
            IconButton(
                onClick = {
                    if (favList[gameIndex] == 0) {
                        favList[gameIndex] = 1
                    } else {
                        favList[gameIndex] = 0
                    }
                },
                modifier = Modifier.width(40.dp)
            ) {
                if (favList[gameIndex] == 0) {
                    Icon(
                        painter = painterResource(R.drawable.kid_star_24px),
                        contentDescription = "Like"
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.outline_family_star_24),
                        contentDescription = "Like"
                    )
                }
            }
        }
    }
}