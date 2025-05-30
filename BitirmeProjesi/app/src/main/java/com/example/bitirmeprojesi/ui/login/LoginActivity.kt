package com.example.bitirmeprojesi.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bitirmeprojesi.databinding.ActivityLoginBinding
import com.example.bitirmeprojesi.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sharedPreferences = getSharedPreferences("GetFoodPrefs", MODE_PRIVATE)
        
        setupUI()
        setupClickListeners()
    }
    
    private fun setupUI() {
        // Material Design TextInputLayout ile şifre görünürlüğü otomatik olarak yönetilir
        // Ek bir toggle butonuna ihtiyaç yok
    }
    
    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            handleLogin()
        }
        
        binding.buttonGuest.setOnClickListener {
            handleGuestLogin()
        }
    }
    
    private fun handleLogin() {
        val username = binding.editTextUsername.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        
        if (username.isEmpty()) {
            Toast.makeText(this, "Kullanıcı adı boş olamaz", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (password.isEmpty()) {
            Toast.makeText(this, "Şifre boş olamaz", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Save login status
        sharedPreferences.edit()
            .putBoolean("isLoggedIn", true)
            .putBoolean("isGuest", false)
            .putString("username", username)
            .apply()
        
        Log.d("LoginActivity", "User logged in: username=$username, isGuest=false")
        Toast.makeText(this, "Giriş başarılı! Hoş geldiniz $username", Toast.LENGTH_SHORT).show()
        
        navigateToHome()
    }
    
    private fun handleGuestLogin() {
        // Save guest status
        sharedPreferences.edit()
            .putBoolean("isLoggedIn", true)
            .putBoolean("isGuest", true)
            .apply()
        
        navigateToHome()
    }
    
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
