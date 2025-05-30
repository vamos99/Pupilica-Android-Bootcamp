package com.example.bitirmeprojesi.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.api.RetrofitClient
import com.example.bitirmeprojesi.data.model.SepetYemek
import com.example.bitirmeprojesi.databinding.ActivityCartBinding
import com.example.bitirmeprojesi.ui.home.HomeActivity
import com.example.bitirmeprojesi.ui.profile.ProfileActivity
import com.example.bitirmeprojesi.utils.CartBadgeHelper
import com.google.gson.Gson
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private var cartList = mutableListOf<SepetYemek>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupBottomNavigation()
        setupToolbar()
        loadCart()
        CartBadgeHelper.updateCartBadge(binding.bottomNavigation, this, lifecycleScope)

        binding.btnCheckout.setOnClickListener {
            val total = cartList.sumOf { it.yemek_fiyat.toInt() * it.yemek_siparis_adet.toInt() }
            val intent = Intent(this, com.example.bitirmeprojesi.ui.payment.PaymentActivity::class.java)
            intent.putExtra("totalPrice", total.toString())
            startActivity(intent)
        }

        binding.btnBrowseFoods.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onQuantityChanged = { item, newQty ->
                updateCartItem(item, newQty)
            },
            onDeleteClicked = { item ->
                deleteCartItem(item)
            }
        )
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCart.adapter = cartAdapter
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnClearCartIcon.setOnClickListener {
            if (cartList.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Sepeti Temizle")
                    .setMessage("Tüm ürünleri silmek istediğinize emin misiniz?")
                    .setPositiveButton("Evet") { _, _ -> clearCart() }
                    .setNegativeButton("Hayır", null)
                    .show()
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_cart
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_cart -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadCart() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.sepettekiYemekleriGetirRaw("halil_kiyak")
                val body = response.body()?.string()
                if (body.isNullOrBlank() || body.length < 10) {
                    showEmptyCart()
                    return@launch
                }
                val cartResponse = Gson().fromJson(body, com.example.bitirmeprojesi.data.model.SepetYemeklerResponse::class.java)
                if (cartResponse.success == 1 && !cartResponse.sepet_yemekler.isNullOrEmpty()) {
                    cartList = cartResponse.sepet_yemekler.toMutableList()
                    cartAdapter.submitList(cartList.toList())
                    updateTotalPrice()
                    showCartContent()
                } else {
                    showEmptyCart()
                }
            } catch (e: Exception) {
                showEmptyCart()
            }
        }
    }

    private fun updateCartItem(item: SepetYemek, newQty: Int) {
        lifecycleScope.launch {
            try {
                if (newQty < 1) return@launch
                val response = RetrofitClient.api.sepeteYemekEkle(
                    item.yemek_adi,
                    item.yemek_resim_adi,
                    item.yemek_fiyat,
                    newQty.toString(),
                    "halil_kiyak"
                )
                if (response.success == 1) {
                    loadCart()
                    CartBadgeHelper.updateCartBadge(binding.bottomNavigation, this@CartActivity, lifecycleScope)
                }
            } catch (_: Exception) {}
        }
    }

    private fun deleteCartItem(item: SepetYemek) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.sepettenYemekSil(item.sepet_yemek_id, "halil_kiyak")
                if (response.success == 1) {
                    loadCart()
                    CartBadgeHelper.updateCartBadge(binding.bottomNavigation, this@CartActivity, lifecycleScope)
                }
            } catch (_: Exception) {}
        }
    }

    private fun clearCart() {
        lifecycleScope.launch {
            try {
                // Tüm ürünleri sil
                val response = RetrofitClient.api.sepettekiYemekleriGetirRaw("halil_kiyak")
                val body = response.body()?.string()
                if (!body.isNullOrBlank() && body.length > 10) {
                    val cartResponse = Gson().fromJson(body, com.example.bitirmeprojesi.data.model.SepetYemeklerResponse::class.java)
                    cartResponse.sepet_yemekler?.forEach { item ->
                        RetrofitClient.api.sepettenYemekSil(item.sepet_yemek_id, "halil_kiyak")
                    }
                }
                loadCart()
                CartBadgeHelper.updateCartBadge(binding.bottomNavigation, this@CartActivity, lifecycleScope)
            } catch (_: Exception) {}
        }
    }

    private fun updateTotalPrice() {
        val total = cartList.sumOf { it.yemek_fiyat.toInt() * it.yemek_siparis_adet.toInt() }
        binding.textViewTotalPrice.text = "${total}₺"
    }

    private fun showEmptyCart() {
        binding.cartContentLayout.visibility = View.GONE
        binding.emptyCartLayout.visibility = View.VISIBLE
        binding.textViewTotalPrice.text = "0₺"
    }

    private fun showCartContent() {
        binding.cartContentLayout.visibility = View.VISIBLE
        binding.emptyCartLayout.visibility = View.GONE
    }
}
