package com.insa.mygameslist.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.insa.mygameslist.R

object IGDB {

    lateinit var covers: List<Cover>
    lateinit var games: List<Games>
    lateinit var genres: List<Genres>
    lateinit var platforms: List<Platforms>
    lateinit var platformsLogos: List<PlatformsLogos>
    fun load(context: Context) {
        val coversFromJson: List<Cover> = Gson().fromJson(
            context.resources.openRawResource(R.raw.covers).bufferedReader(),
            object : TypeToken<List<Cover>>() {}.type
        )
        covers = coversFromJson

        val genresFromJson: List<Genres> = Gson().fromJson(
            context.resources.openRawResource(R.raw.genres).bufferedReader(),
            object : TypeToken<List<Genres>>() {}.type
        )
        genres = genresFromJson

        val platformsFromJson: List<Platforms> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platforms).bufferedReader(),
            object : TypeToken<List<Platforms>>() {}.type
        )
        platforms = platformsFromJson

        val platformsLogosFromJson: List<PlatformsLogos> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platform_logos).bufferedReader(),
            object : TypeToken<List<PlatformsLogos>>() {}.type
        )
        platformsLogos = platformsLogosFromJson

        val gamesFromJson: List<Games> = Gson().fromJson(
            context.resources.openRawResource(R.raw.games).bufferedReader(),
            object : TypeToken<List<Games>>() {}.type
        )

        games = gamesFromJson

    }
}

data class Cover(val id: Long, val url: String)
data class Games(
    val id: Long,
    val cover: Long,
    val first_release_date: Long,
    val genres: List<Int>,
    val name: String,
    val platforms: List<Int>,
    val summary: String,
    val total_rating: Double
)

data class Genres(val id: Int, val name: String)
data class PlatformsLogos(val id: Int, val url: String)
data class Platforms(val id: Int, val name: String, val url: Int)