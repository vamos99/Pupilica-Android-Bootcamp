package com.example.bitirmeprojesi.ui.payment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bitirmeprojesi.databinding.ActivityPaymentBinding
import com.example.bitirmeprojesi.ui.home.HomeActivity
import com.example.bitirmeprojesi.utils.CartBadgeHelper
import com.example.bitirmeprojesi.data.api.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.launch
import com.example.bitirmeprojesi.data.model.Adres
import com.google.gson.reflect.TypeToken
import android.widget.ArrayAdapter
import android.view.View

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var adresList: List<Adres>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val totalPrice = intent.getStringExtra("totalPrice") ?: "0"
        binding.tvTotalPrice.text = "₺$totalPrice"

        binding.btnPay.setOnClickListener {
            val address = binding.etAddress.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            if (address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Adres ve telefon boş olamaz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            confirmOrder()
        }

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnSelectAddress.setOnClickListener {
            //android.util.Log.d("PaymentActivity", "Kayıtlı Adreslerimden Seç butonuna tıklandı")
            loadAddresses()
            if (adresList.isNotEmpty()) {
                val spinner = binding.spinnerAddresses
                spinner.visibility = View.VISIBLE
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, adresList.map { it.baslik })
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.performClick()
                spinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                        val selected = adresList[position]
                        binding.etAddress.setText(selected.adres)
                        binding.etPhone.setText(selected.telefon)
                        spinner.visibility = View.GONE
                    }
                    override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
                }
            } else {
                android.widget.Toast.makeText(this, "Kayıtlı adres bulunamadı.", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // BottomNavigation aktif olsun
        binding.bottomNavigation.selectedItemId = com.example.bitirmeprojesi.R.id.nav_cart
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                com.example.bitirmeprojesi.R.id.nav_home -> {
                    startActivity(Intent(this, com.example.bitirmeprojesi.ui.home.HomeActivity::class.java))
                    finish()
                    true
                }
                com.example.bitirmeprojesi.R.id.nav_cart -> true
                com.example.bitirmeprojesi.R.id.nav_profile -> {
                    startActivity(Intent(this, com.example.bitirmeprojesi.ui.profile.ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun confirmOrder() {
        lifecycleScope.launch {
            try {
                // Sepeti tamamen sil
                val response = RetrofitClient.api.sepettekiYemekleriGetirRaw("halil_kiyak")
                val body = response.body()?.string()
                if (!body.isNullOrBlank() && body.length > 10) {
                    val cartResponse = Gson().fromJson(body, com.example.bitirmeprojesi.data.model.SepetYemeklerResponse::class.java)
                    cartResponse.sepet_yemekler?.forEach { item ->
                        RetrofitClient.api.sepettenYemekSil(item.sepet_yemek_id, "halil_kiyak")
                    }
                }
                CartBadgeHelper.updateCartBadge(binding.bottomNavigation, this@PaymentActivity, lifecycleScope)
                AlertDialog.Builder(this@PaymentActivity)
                    .setTitle("Sipariş Tamamlandı")
                    .setMessage("Siparişiniz tamamlanmıştır!")
                    .setPositiveButton("Tamam") { _, _ ->
                        val intent = Intent(this@PaymentActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    .setCancelable(false)
                    .show()
            } catch (_: Exception) {
                Toast.makeText(this@PaymentActivity, "Sipariş tamamlanamadı!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadAddresses() {
        val prefs = getSharedPreferences("GetFoodPrefs", MODE_PRIVATE)
        val json = prefs.getString("addresses", null)
        adresList = if (!json.isNullOrBlank()) {
            val type = object : TypeToken<List<Adres>>() {}.type
            Gson().fromJson(json, type)
        } else emptyList()
    }
} 