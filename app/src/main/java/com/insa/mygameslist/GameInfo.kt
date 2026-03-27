package com.insa.mygameslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.insa.mygameslist.data.IGDB

class GameInfo {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Content(backstack: SnapshotStateList<Any>, nbGame: Int, favList: SnapshotStateList<Int>) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(),
                    title = { Text(IGDB.games[nbGame].name) },
                    navigationIcon = {
                        IconButton(onClick = { backstack.removeLastOrNull() }) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_arrow_back_24),
                                contentDescription = "back Arrow",
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                if (favList[nbGame] == 0) {
                                    favList[nbGame] = 1
                                } else {
                                    favList[nbGame] = 0
                                }
                            },
                            modifier = Modifier.width(40.dp)
                        ) {
                            if (favList[nbGame] == 0) {
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
                )
            },
            contentWindowInsets = WindowInsets.systemBars,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            InfoFromGame(innerPadding, nbGame)
        }
    }

    @Composable
    fun InfoFromGame(innerPadding: PaddingValues, nbGame: Int) {
        val index = IGDB.games[nbGame].cover
        val coverUrl = "https:" + IGDB.covers.first { it.id == index }.url
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            item {
                Text(
                    IGDB.games[nbGame].name,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                )
            }
            item {
                AsyncImage(
                    model = coverUrl,
                    contentDescription = "Cover",
                    modifier = Modifier.height(360.dp)
                )
            }
            var genresStr = ""
            for (idGenre in IGDB.games[nbGame].genres) {
                genresStr += try {
                    IGDB.genres.first { it.id == idGenre }.name + ", "
                } catch (_: NoSuchElementException) {
                    ""
                }
            }
            item {
                Text(genresStr, fontStyle = FontStyle.Italic, fontSize = 10.sp)
            }

            item {
                LazyRow(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (idPlat in IGDB.games[nbGame].platforms) {
                        val platIndex = try {
                            IGDB.platforms.first { it.id == idPlat }.platform_logo
                        } catch (_: NoSuchElementException) {
                            0
                        }
                        if (platIndex != 0) {
                            val platUrl =
                                "https:" + IGDB.platformsLogos.first { it.id == platIndex }.url
                            item {
                                AsyncImage(
                                    model = platUrl,
                                    contentDescription = "Plateform$idPlat",
                                    modifier = Modifier.height(40.dp)
                                )
                            }
                        } else {
                            item {
                                Icon(
                                    painter = painterResource(R.drawable.no_photography_24px),
                                    contentDescription = "Plateform$idPlat",
                                    modifier = Modifier.height(40.dp)
                                )
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    IGDB.games[nbGame].summary,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}