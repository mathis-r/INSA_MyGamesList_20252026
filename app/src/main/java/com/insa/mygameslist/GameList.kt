package com.insa.mygameslist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.insa.mygameslist.data.Games
import com.insa.mygameslist.data.IGDB

class GameList {


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SimpleSearchBar(
        textFieldState: TextFieldState,
        onSearch: (String) -> Unit,
        searchResults: List<Games>,
        modifier: Modifier = Modifier
    ) {
        // Controls expansion state of the search bar
        var expanded by rememberSaveable { mutableStateOf(false) }

        /*Box(
            modifier
                .fillMaxSize()
                .semantics { isTraversalGroup = true }
        ) {*/
            SearchBar(
                modifier = Modifier
                    //.align(Alignment.TopCenter)
                    .semantics { traversalIndex = 0f },
                inputField = {
                    SearchBarDefaults.InputField(
                        query = textFieldState.text.toString(),
                        onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                        onSearch = {
                            onSearch(textFieldState.text.toString())
                            expanded = false
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text("Search") }
                    )
                },
                expanded = false,
                onExpandedChange = { expanded = false },
            ) {/*
                // Display search results in a scrollable column
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    searchResults.forEach { result ->
                        ListItem(
                            headlineContent = { Text(result) },
                            modifier = Modifier
                                .clickable {
                                    textFieldState.edit { replace(0, length, result) }
                                    expanded = false
                                }
                                .fillMaxWidth()
                        )
                    }
                }
            */}
        //}
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Content(backStack: SnapshotStateList<Any>) {
        val textFieldState = rememberTextFieldState()
        val games = IGDB.games

        // Filter items based on the current search text
        val filteredItems by remember {
            derivedStateOf {
                val searchText = textFieldState.text.toString()
                if (searchText.isEmpty()) {
                    games
                } else {
                    games.filter {
                        it.name.contains(
                            searchText,
                            ignoreCase = true
                        ) || try {
                            it.genres.contains(IGDB.genres.first {
                                it.name.contains(
                                    searchText,
                                    ignoreCase = true
                                )
                            }.id)
                        } catch (_: NoSuchElementException) {
                            false
                        } || try {
                            it.platforms.contains(IGDB.platforms.first {
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
                //or it.genres.first{ IGDB.genres[it].name.contains(searchText, ignoreCase = true) }
            }
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = Color.LightGray,
                        titleContentColor = Color.Black,
                    ),
                    title = {
                        SimpleSearchBar(textFieldState, onSearch = {}, filteredItems)
                        //Text("My Games List")
                    }
                )
            },
            contentWindowInsets = WindowInsets.systemBars,
            //modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            ListGame(innerPadding, backStack, filteredItems)
            //InfoFromGame(innerPadding,IGDB.games.first().id)
        }
    }

    @Composable
    fun ListGame(innerPadding: PaddingValues, backStack: SnapshotStateList<Any>, listGame : List<Games>) {
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
        ) {
            //Text("À remplir", modifier = Modifier.padding(innerPadding))
            for (game in listGame){
                item{
                    GameBox(game, innerPadding, backStack)
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
            /*
            for (i in 0..<IGDB.games.size) {
                item {
                    GameBox(i, innerPadding, backStack)
                }
            }*/
        }
    }

    @Composable
    fun GameBox(game: Games, innerPadding: PaddingValues, backStack: SnapshotStateList<Any>) {
        val indices = game.genres

        var genresStr: String = "Genres : "
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
                    backStack.add(Game(IGDB.games.indexOf(game)))
                }),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            AsyncImage(model = coverUrl, contentDescription = "Cover")
            Column(
                modifier = Modifier
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(game.name)
                Text(genresStr)
            }
        }
    }
}