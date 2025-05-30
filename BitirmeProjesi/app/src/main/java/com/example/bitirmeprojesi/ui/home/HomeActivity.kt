package com.example.bitirmeprojesi.ui.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bitirmeprojesi.MainActivity
import com.example.bitirmeprojesi.data.api.RetrofitClient
import com.example.bitirmeprojesi.data.model.Yemek
import com.example.bitirmeprojesi.databinding.ActivityHomeBinding
import com.example.bitirmeprojesi.ui.cart.CartActivity
import com.example.bitirmeprojesi.ui.detail.DetailActivity
import com.example.bitirmeprojesi.ui.profile.ProfileActivity
import com.example.bitirmeprojesi.utils.CartBadgeHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope

class HomeActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var foodAdapter: FoodAdapter
    private var allFoods = listOf<Yemek>()
    private var isGuest = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sharedPreferences = getSharedPreferences("GetFoodPrefs", MODE_PRIVATE)
        isGuest = sharedPreferences.getBoolean("isGuest", false)
        
        setupRecyclerView()
        setupSearchView()
        setupBottomNavigation()
        loadFoods()
        updateCartBadge()
    }
    
    override fun onResume() {
        super.onResume()
        // Her dönüşte sepet badge'ini güncelle
        updateCartBadge()
    }
    
    private fun updateCartBadge() {
        CartBadgeHelper.updateCartBadge(binding.bottomNavigation, this, lifecycleScope)
    }
    
    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter(
            onItemClick = { yemek ->
                // Navigate to food detail
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("yemek_id", yemek.yemek_id)
                intent.putExtra("yemek_adi", yemek.yemek_adi)
                intent.putExtra("yemek_resim_adi", yemek.yemek_resim_adi)
                intent.putExtra("yemek_fiyat", yemek.yemek_fiyat)
                startActivity(intent)
            },
            onFavoriteClick = { yemek ->
                handleFavoriteClick(yemek)
            },
            isGuestUser = isGuest
        )
        
        binding.recyclerViewFoods.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = foodAdapter
        }
    }
    
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                filterFoods(newText ?: "")
                return true
            }
        })
    }
    
    private fun filterFoods(query: String) {
        val filteredList = if (query.isEmpty()) {
            allFoods
        } else {
            allFoods.filter { yemek ->
                yemek.yemek_adi.contains(query, ignoreCase = true)
            }
        }
        foodAdapter.submitList(filteredList)
        
        binding.textEmptyState.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
    }
    
    private fun loadFoods() {
        binding.progressBar.visibility = View.VISIBLE
        binding.textEmptyState.visibility = View.GONE
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("HomeActivity", "API çağrısı başlatıldı...")
                val response = RetrofitClient.api.tumYemekleriGetir()
                Log.d("HomeActivity", "API yanıtı alındı: ${response.yemekler.size} yemek")
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    allFoods = response.yemekler
                    foodAdapter.submitList(allFoods)
                    
                    if (allFoods.isEmpty()) {
                        binding.textEmptyState.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeActivity", "API hatası: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.textEmptyState.text = "Bağlantı hatası: ${e.message}"
                    binding.textEmptyState.visibility = View.VISIBLE
                    Toast.makeText(this@HomeActivity, "Yemekler yüklenemedi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun handleFavoriteClick(yemek: Yemek) {
        if (isGuest) {
            Toast.makeText(this, "Favorilere eklemek için giriş yapın", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Use GetFoodPrefs to be consistent with other activities
        val prefs = getSharedPreferences("GetFoodPrefs", MODE_PRIVATE)
        val favorites = prefs.getStringSet("favorites", mutableSetOf()) ?: mutableSetOf()
        val newFavorites = favorites.toMutableSet()
        val favoriteString = "${yemek.yemek_id}|${yemek.yemek_adi}|${yemek.yemek_resim_adi}|${yemek.yemek_fiyat}"
        
        if (newFavorites.contains(favoriteString)) {
            newFavorites.remove(favoriteString)
            Toast.makeText(this, "${yemek.yemek_adi} favorilerden çıkarıldı", Toast.LENGTH_SHORT).show()
        } else {
            newFavorites.add(favoriteString)
            Toast.makeText(this, "${yemek.yemek_adi} favorilere eklendi", Toast.LENGTH_SHORT).show()
        }
        
        prefs.edit()
            .putStringSet("favorites", newFavorites)
            .apply()
        
        // Refresh the adapter to update favorite icons
        foodAdapter.notifyDataSetChanged()
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = com.example.bitirmeprojesi.R.id.nav_home
        
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                com.example.bitirmeprojesi.R.id.nav_home -> {
                    // Already on home
                    true
                }
                com.example.bitirmeprojesi.R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                com.example.bitirmeprojesi.R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
    
    private fun logout() {
        sharedPreferences.edit()
            .clear()
            .apply()
        
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
