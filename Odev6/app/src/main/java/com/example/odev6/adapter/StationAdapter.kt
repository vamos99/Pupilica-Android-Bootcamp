package com.example.odev6.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.odev6.databinding.ItemStationBinding
import com.example.odev6.model.StationItem

class StationAdapter(private val stationList: List<StationItem>) : RecyclerView.Adapter<StationAdapter.StationViewHolder>() {

    inner class StationViewHolder(private val binding: ItemStationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: StationItem) {
            binding.stationImage.setImageResource(station.image)
            binding.stationName.text = station.name
            binding.stationArtists.text = station.artists
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val binding = ItemStationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind(stationList[position])
    }

    override fun getItemCount() = stationList.size
} 