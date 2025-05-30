package com.example.bitirmeprojesi.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.model.Yemek
import com.example.bitirmeprojesi.databinding.ItemFoodBinding

class FoodAdapter(
    private val onItemClick: (Yemek) -> Unit,
    private val onFavoriteClick: (Yemek) -> Unit,
    private val isGuestUser: Boolean
) : ListAdapter<Yemek, FoodAdapter.FoodViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Yemek>() {
            override fun areItemsTheSame(oldItem: Yemek, newItem: Yemek): Boolean {
                return oldItem.yemek_id == newItem.yemek_id
            }

            override fun areContentsTheSame(oldItem: Yemek, newItem: Yemek): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FoodViewHolder(private val binding: ItemFoodBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(yemek: Yemek) {
            binding.textViewFoodName.text = yemek.yemek_adi
            binding.textViewFoodPrice.text = "₺${yemek.yemek_fiyat}"

            // Load image with Glide
            val imageUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}"
            Glide.with(binding.imageViewFood.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .into(binding.imageViewFood)

            // Handle favorite button
            if (isGuestUser) {
                binding.buttonFavorite.alpha = 0.5f
                binding.buttonFavorite.isEnabled = false
                binding.buttonFavorite.setImageResource(R.drawable.ic_favorite_border)
                binding.buttonFavorite.setColorFilter(
                    binding.root.context.getColor(R.color.neutral_light), android.graphics.PorterDuff.Mode.SRC_IN)
            } else {
                binding.buttonFavorite.alpha = 1.0f
                binding.buttonFavorite.isEnabled = true
                
                // Check if this item is in favorites
                val context = binding.root.context
                val prefs = context.getSharedPreferences("GetFoodPrefs", android.content.Context.MODE_PRIVATE)
                val favorites = prefs.getStringSet("favorites", mutableSetOf()) ?: mutableSetOf()
                val favoriteString = "${yemek.yemek_id}|${yemek.yemek_adi}|${yemek.yemek_resim_adi}|${yemek.yemek_fiyat}"
                
                if (favorites.contains(favoriteString)) {
                    binding.buttonFavorite.setImageResource(R.drawable.ic_favorite)
                    binding.buttonFavorite.setColorFilter(
                        context.getColor(R.color.primary_mor), android.graphics.PorterDuff.Mode.SRC_IN)
                } else {
                    binding.buttonFavorite.setImageResource(R.drawable.ic_favorite_border)
                    binding.buttonFavorite.setColorFilter(
                        context.getColor(R.color.neutral_light), android.graphics.PorterDuff.Mode.SRC_IN)
                }
            }

            // Click listeners
            binding.root.setOnClickListener {
                onItemClick(yemek)
            }

            binding.buttonFavorite.setOnClickListener {
                if (!isGuestUser) {
                    onFavoriteClick(yemek)
                }
            }
        }
    }
}
