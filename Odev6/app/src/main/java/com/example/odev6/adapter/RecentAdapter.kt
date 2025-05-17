package com.example.odev6.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.odev6.databinding.ItemRecentBinding
import com.example.odev6.model.RecentItem

class RecentAdapter(private val recentList: List<RecentItem>) : RecyclerView.Adapter<RecentAdapter.RecentViewHolder>() {

    inner class RecentViewHolder(private val binding: ItemRecentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recent: RecentItem) {
            binding.recentImage.setImageResource(recent.image)
            binding.recentName.text = recent.name
            binding.recentArtists.text = recent.artists
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val binding = ItemRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(recentList[position])
    }

    override fun getItemCount() = recentList.size
} 