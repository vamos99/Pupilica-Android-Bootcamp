package com.example.odev6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odev6.adapter.MixAdapter
import com.example.odev6.adapter.StationAdapter
import com.example.odev6.adapter.RecentAdapter
import com.example.odev6.model.MixItem
import com.example.odev6.model.StationItem
import com.example.odev6.model.RecentItem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.mixRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = MixAdapter(getMixes())

        val stationRecyclerView = findViewById<RecyclerView>(R.id.stationRecyclerView)
        stationRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        stationRecyclerView.adapter = StationAdapter(getStations())

        val recentRecyclerView = findViewById<RecyclerView>(R.id.recentRecyclerView)
        recentRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recentRecyclerView.adapter = RecentAdapter(getRecents())
    }

    private fun getMixes(): List<MixItem> = listOf(
        MixItem("2020s Mix", "AYLIVA, Central Cee, OneRepublic ve daha fazlası", R.drawable.img1),
        MixItem("J-Pop Mix", "Goose house, King Gnu", R.drawable.img1),
        MixItem("Rock Mix", "Evanescence, Duman, Şanlısoy", R.drawable.img1),
        MixItem("Pop Mix", "Tarkan, Sezen Aksu", R.drawable.img1),
        MixItem("Indie Mix", "Mor ve Ötesi, Adamlar", R.drawable.img1)
    )

    private fun getStations(): List<StationItem> = listOf(
        StationItem("All The Way", "Morgan Wallen, BigXthaPlug, Shaboozey", R.drawable.img1),
        StationItem("Ma Meilleure Ennemie ft. Coldplay", "Stray Kids, Arcane, League of Legends, Stray Kids", R.drawable.img1),
        StationItem("Dojacat", "The Weeknd, Post Malone, Doja Cat", R.drawable.img1)
    )

    private fun getRecents(): List<RecentItem> = listOf(
        RecentItem("Haftalık Keşif", "Çalma Listesi", R.drawable.img1),
        RecentItem("Yeni Müzik Radarı", "Çalma Listesi", R.drawable.img1),
        RecentItem("Yeni Müzik Radarı", "Çalma Listesi", R.drawable.img1),
        RecentItem("Yeni Müzik Radarı", "Çalma Listesi", R.drawable.img1),
        RecentItem("Yeni Müzik Radarı", "Çalma Listesi", R.drawable.img1)
    )
}