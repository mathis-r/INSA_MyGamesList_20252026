package com.insa.mygameslist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.insa.mygameslist.data.IGDB
import com.insa.mygameslist.ui.theme.MyGamesListTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.load(this)

        enableEdgeToEdge()
        setContent {

            MyGamesListTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = topAppBarColors(
                                containerColor = Color.Magenta,
                                titleContentColor = Color.Black,
                            ),
                            title = { Text("My Games List") })
                    },
                    contentWindowInsets = WindowInsets.systemBars,
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        //Text("À remplir", modifier = Modifier.padding(innerPadding))

                        for (i in 0..<IGDB.games.size) {
                            item {
                                GameBox(i, innerPadding)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun GameBox(nbGame: Int, innerPadding: PaddingValues) {
        val indeces = IGDB.games[nbGame].genres
        var genresStr: String = "Genres : "
        for (i in 0..<indeces.size) {
            try {
                genresStr+=IGDB.genres.first { it.id == indeces[i] }.name
                genresStr+=", "
            } catch (_: NoSuchElementException){}
        }
        val index = IGDB.games[nbGame].cover
        val coverUrl = "https:"+ IGDB.covers.first { it.id == index }.url
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            AsyncImage(model=coverUrl, contentDescription = "Cover")
            Column(
                modifier = Modifier
                    .padding(12.dp),
                Arrangement.SpaceEvenly
            ) {
                Text(IGDB.games[nbGame].name)
                Text(genresStr)
            }
        }
    }
}