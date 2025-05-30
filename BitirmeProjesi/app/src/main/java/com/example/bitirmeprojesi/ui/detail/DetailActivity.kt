package com.example.bitirmeprojesi.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.api.RetrofitClient
import com.example.bitirmeprojesi.data.model.Yemek
import com.example.bitirmeprojesi.databinding.ActivityDetailBinding
import kotlinx.coroutines.launch
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityDetailBinding
    private var currentQuantity = 1
    private var yemek: Yemek? = null
    private var isGuest = false
    private var userName = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Get user info
        val prefs = getSharedPreferences("GetFoodPrefs", MODE_PRIVATE)
        isGuest = prefs.getBoolean("isGuest", true)
        userName = prefs.getString("username", "guest") ?: "guest"
        
        getYemekFromIntent()
        setupUI()
        setupClickListeners()
    }
    
    private fun getYemekFromIntent() {
        yemek = Yemek(
            yemek_id = intent.getStringExtra("yemek_id") ?: "",
            yemek_adi = intent.getStringExtra("yemek_adi") ?: "",
            yemek_resim_adi = intent.getStringExtra("yemek_resim_adi") ?: "",
            yemek_fiyat = intent.getStringExtra("yemek_fiyat") ?: "0"
        )
    }
    
    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Ürün Detayı"
        
        yemek?.let { food ->
            binding.textViewFoodName.text = food.yemek_adi
            binding.textViewUnitPrice.text = "₺${food.yemek_fiyat}"
            
            // Load image
            val imageUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}"
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .into(binding.imageViewFood)
        }
        
        updateQuantityAndPrice()
    }
    
    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        binding.buttonDecrease.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity--
                updateQuantityAndPrice()
            }
        }
        
        binding.buttonIncrease.setOnClickListener {
            currentQuantity++
            updateQuantityAndPrice()
        }
        
        binding.buttonAddToCart.setOnClickListener {
            addToCart()
        }
    }
    
    private fun updateQuantityAndPrice() {
        binding.textViewQuantity.text = currentQuantity.toString()
        
        yemek?.let { food ->
            val unitPrice = food.yemek_fiyat.toDoubleOrNull() ?: 0.0
            val totalPrice = unitPrice * currentQuantity
            binding.textViewTotalPrice.text = "₺%.2f".format(totalPrice)
            binding.buttonAddToCart.text = "Sepete Ekle - ₺%.2f".format(totalPrice)
        }
    }
    
    private fun addToCart() {
        if (isGuest) {
            Toast.makeText(this, "Sepete ürün eklemek için giriş yapmanız gerekiyor", Toast.LENGTH_LONG).show()
            return
        }
        
        yemek?.let { food ->
            lifecycleScope.launch {
                try {
                    // API'ye her zaman "halil_kiyak" username'i ile gönder (rules.txt'e göre)
                    val apiUsername = "halil_kiyak"
                    
                    // Önce sepetteki mevcut ürünleri kontrol et
                    val cartRawResponse = RetrofitClient.api.sepettekiYemekleriGetirRaw(apiUsername)
                    val cartBody = cartRawResponse.body()?.string()
                    if (cartBody.isNullOrBlank() || cartBody.length < 10) {
                        // Sepet boş, direkt ekle
                        val response = RetrofitClient.api.sepeteYemekEkle(
                            food.yemek_adi,
                            food.yemek_resim_adi,
                            food.yemek_fiyat,
                            currentQuantity.toString(),
                            apiUsername
                        )
                        if (response.success == 1) {
                            Toast.makeText(this@DetailActivity, "${food.yemek_adi} sepete eklendi ($currentQuantity adet)", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@DetailActivity, "Ürün sepete eklenirken hata oluştu: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                        return@launch
                    }
                    val cartResponse = Gson().fromJson(cartBody, com.example.bitirmeprojesi.data.model.SepetYemeklerResponse::class.java)
                    
                    if (cartResponse.success == 1 && !cartResponse.sepet_yemekler.isNullOrEmpty()) {
                        // Aynı ürünün sepette olup olmadığını kontrol et
                        val existingItem = cartResponse.sepet_yemekler.find { it.yemek_adi == food.yemek_adi }
                        
                        if (existingItem != null) {
                            // Ürün zaten sepette var, miktarını artır
                            // Önce eski ürünü sil
                            val deleteResponse = RetrofitClient.api.sepettenYemekSil(existingItem.sepet_yemek_id, apiUsername)
                            
                            // Yeni miktarla tekrar ekle
                            val newQuantity = existingItem.yemek_siparis_adet.toInt() + currentQuantity
                            
                            val response = RetrofitClient.api.sepeteYemekEkle(
                                food.yemek_adi,
                                food.yemek_resim_adi,
                                food.yemek_fiyat,
                                newQuantity.toString(),
                                apiUsername
                            )
                            
                            if (response.success == 1) {
                                Toast.makeText(this@DetailActivity, "${food.yemek_adi} sepetindeki miktarı güncellendi (Toplam: $newQuantity adet)", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@DetailActivity, "Ürün miktarı güncellenirken hata oluştu: ${response.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Ürün sepette yok, yeni olarak ekle
                            val response = RetrofitClient.api.sepeteYemekEkle(
                                food.yemek_adi,
                                food.yemek_resim_adi,
                                food.yemek_fiyat,
                                currentQuantity.toString(),
                                apiUsername
                            )
                            
                            if (response.success == 1) {
                                Toast.makeText(this@DetailActivity, "${food.yemek_adi} sepete eklendi ($currentQuantity adet)", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@DetailActivity, "Ürün sepete eklenirken hata oluştu: ${response.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Sepet boş, direkt ekle
                        val response = RetrofitClient.api.sepeteYemekEkle(
                            food.yemek_adi,
                            food.yemek_resim_adi,
                            food.yemek_fiyat,
                            currentQuantity.toString(),
                            apiUsername
                        )
                        
                        if (response.success == 1) {
                            Toast.makeText(this@DetailActivity, "${food.yemek_adi} sepete eklendi ($currentQuantity adet)", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@DetailActivity, "Ürün sepete eklenirken hata oluştu: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    
                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, "Ürün sepete eklenirken hata oluştu: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
