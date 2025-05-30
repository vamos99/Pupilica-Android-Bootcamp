package com.example.bitirmeprojesi.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bitirmeprojesi.MainActivity
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.databinding.ActivityProfileBinding
import com.example.bitirmeprojesi.ui.cart.CartActivity
import com.example.bitirmeprojesi.ui.favorites.FavoritesActivity
import com.example.bitirmeprojesi.ui.home.HomeActivity
import com.example.bitirmeprojesi.ui.login.LoginActivity
import com.example.bitirmeprojesi.utils.CartBadgeHelper
import androidx.lifecycle.lifecycleScope

class ProfileActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProfileBinding
    private var isGuest = false
    private var userName = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Get user info from SharedPreferences
        val prefs = getSharedPreferences("GetFoodPrefs", MODE_PRIVATE)
        isGuest = prefs.getBoolean("isGuest", true)
        userName = prefs.getString("username", "Misafir") ?: "Misafir"
        
        //Log.d("ProfileActivity", "User info loaded: isGuest=$isGuest, userName=$userName")
        
        setupUI()
        setupClickListeners()
        setupBottomNavigation()
        updateCartBadge()
    }
    
    override fun onResume() {
        super.onResume()
        updateCartBadge()
    }
    
    private fun updateCartBadge() {
        CartBadgeHelper.updateCartBadge(binding.bottomNavigation, this, lifecycleScope)
    }
    
    private fun setupUI() {
        if (isGuest) {
            binding.tvUserName.text = "Misafir Kullanıcı"
            binding.tvUserStatus.text = "Misafir Modunda"
            // Disable features for guest users
            binding.layoutAddresses.alpha = 0.5f
            binding.layoutFavorites.alpha = 0.5f
        } else {
            binding.tvUserName.text = userName
            binding.tvUserStatus.text = "Giriş Yapıldı"
        }
    }
     private fun setupClickListeners() {
        binding.layoutAddresses.setOnClickListener {
            //Log.d("ProfileActivity", "Adreslerim seçeneğine tıklandı. isGuest=$isGuest")
            if (!isGuest) {
                val intent = Intent(this, com.example.bitirmeprojesi.ui.address.AddressActivity::class.java)
                startActivity(intent)
            } else {
                showGuestMessage()
            }
        }
        
        binding.layoutFavorites.setOnClickListener {
            if (!isGuest) {
                startActivity(Intent(this, FavoritesActivity::class.java))
            } else {
                showGuestMessage()
            }
        }

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_profile
        
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
                R.id.nav_profile -> true
                else -> false
            }
        }
    }
    
    private fun showGuestMessage() {
        AlertDialog.Builder(this)
            .setTitle("Giriş Gerekli")
            .setMessage("Bu özelliği kullanmak için giriş yapmanız gerekiyor.")
            .setPositiveButton("Giriş Yap") { _, _ ->
                // Navigate to LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("İptal", null)
            .show()
    }
    
    private fun showFeatureNotAvailable() {
        AlertDialog.Builder(this)
            .setTitle("Özellik Geliştiriliyor")
            .setMessage("Bu özellik henüz geliştirme aşamasındadır. Yakında kullanıma sunulacaktır.")
            .setPositiveButton("Tamam", null)
            .show()
    }
    
    private fun showLogoutDialog() {
        val message = if (isGuest) {
            "Misafir modundan çıkmak istediğinize emin misiniz?"
        } else {
            "Çıkış yapmak istediğinize emin misiniz?"
        }
        
        AlertDialog.Builder(this)
            .setTitle("Çıkış Yap")
            .setMessage(message)
            .setPositiveButton("Evet") { _, _ ->
                logout()
            }
            .setNegativeButton("İptal", null)
            .show()
    }
    
    private fun logout() {
        // Clear user session
        val prefs = getSharedPreferences("GetFoodPrefs", MODE_PRIVATE)
        with(prefs.edit()) {
            clear()
            apply()
        }
        
        // Navigate to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
