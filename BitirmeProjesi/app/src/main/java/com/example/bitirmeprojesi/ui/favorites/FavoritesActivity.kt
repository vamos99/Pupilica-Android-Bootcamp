package com.example.bitirmeprojesi.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.model.Yemek
import com.example.bitirmeprojesi.databinding.ActivityFavoritesBinding
import com.example.bitirmeprojesi.ui.cart.CartActivity
import com.example.bitirmeprojesi.ui.detail.DetailActivity
import com.example.bitirmeprojesi.ui.home.FoodAdapter
import com.example.bitirmeprojesi.ui.home.HomeActivity
import com.example.bitirmeprojesi.ui.profile.ProfileActivity
import com.example.bitirmeprojesi.utils.CartBadgeHelper
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var favoritesAdapter: FoodAdapter
    private val favoritesList = mutableListOf<Yemek>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupRecyclerView()
        setupClickListeners()
        setupBottomNavigation()
        loadFavorites()
        updateCartBadge()
    }
    
    override fun onResume() {
        super.onResume()
        loadFavorites() // Refresh favorites when returning to this activity
        updateCartBadge()
    }
    
    private fun updateCartBadge() {
        CartBadgeHelper.updateCartBadge(binding.bottomNavigation, this, lifecycleScope)
    }
    
    private fun setupRecyclerView() {
        favoritesAdapter = FoodAdapter(
            onItemClick = { yemek ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("yemek_id", yemek.yemek_id)
                intent.putExtra("yemek_adi", yemek.yemek_adi)
                intent.putExtra("yemek_resim_adi", yemek.yemek_resim_adi)
                intent.putExtra("yemek_fiyat", yemek.yemek_fiyat)
                startActivity(intent)
            },
            onFavoriteClick = { yemek ->
                toggleFavorite(yemek)
            },
            isGuestUser = false // Favorites screen is only for logged in users
        )
        
        binding.recyclerViewFavorites.apply {
            adapter = favoritesAdapter
            layoutManager = GridLayoutManager(this@FavoritesActivity, 2)
        }
    }
    
    private fun setupClickListeners() {
        binding.btnBrowseFoods.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
    
    private fun loadFavorites() {
        lifecycleScope.launch {
            val prefs = getSharedPreferences("GetFoodPrefs", MODE_PRIVATE)
            val favoritesSet = prefs.getStringSet("favorites", emptySet()) ?: emptySet()
            
            if (favoritesSet.isEmpty()) {
                showEmptyState()
            } else {
                // Convert favorites IDs back to Yemek objects
                // Note: In a real app, you'd probably store more complete data or fetch from API
                val favorites = favoritesSet.mapNotNull { favoriteString ->
                    try {
                        val parts = favoriteString.split("|")
                        if (parts.size >= 4) {
                            Yemek(
                                yemek_id = parts[0],
                                yemek_adi = parts[1],
                                yemek_resim_adi = parts[2],
                                yemek_fiyat = parts[3]
                            )
                        } else null
                    } catch (e: Exception) {
                        null
                    }
                }
                
                if (favorites.isEmpty()) {
                    showEmptyState()
                } else {
                    favoritesList.clear()
                    favoritesList.addAll(favorites)
                    favoritesAdapter.submitList(favoritesList)
                    showFavorites()
                }
            }
        }
    }
    
    private fun toggleFavorite(yemek: Yemek) {
        lifecycleScope.launch {
            val prefs = getSharedPreferences("GetFoodPrefs", MODE_PRIVATE)
            val favoritesSet = prefs.getStringSet("favorites", emptySet())?.toMutableSet() ?: mutableSetOf()
            val favoriteString = "${yemek.yemek_id}|${yemek.yemek_adi}|${yemek.yemek_resim_adi}|${yemek.yemek_fiyat}"
            
            if (favoritesSet.contains(favoriteString)) {
                favoritesSet.remove(favoriteString)
            }
            
            with(prefs.edit()) {
                putStringSet("favorites", favoritesSet)
                apply()
            }
            
            // Reload favorites
            loadFavorites()
        }
    }
    
    private fun showEmptyState() {
        binding.emptyFavoritesLayout.visibility = View.VISIBLE
        binding.recyclerViewFavorites.visibility = View.GONE
    }
    
    private fun showFavorites() {
        binding.emptyFavoritesLayout.visibility = View.GONE
        binding.recyclerViewFavorites.visibility = View.VISIBLE
    }
}
