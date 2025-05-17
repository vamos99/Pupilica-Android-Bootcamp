package com.example.odev6.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.odev6.databinding.ItemMixBinding
import com.example.odev6.model.MixItem

class MixAdapter(private val mixList: List<MixItem>) : RecyclerView.Adapter<MixAdapter.MixViewHolder>() {

    inner class MixViewHolder(private val binding: ItemMixBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mix: MixItem) {
            binding.mixImage.setImageResource(mix.image)
            binding.mixName.text = mix.name
            binding.mixArtists.text = mix.artists
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MixViewHolder {
        val binding = ItemMixBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MixViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MixViewHolder, position: Int) {
        holder.bind(mixList[position])
    }

    override fun getItemCount() = mixList.size
} 