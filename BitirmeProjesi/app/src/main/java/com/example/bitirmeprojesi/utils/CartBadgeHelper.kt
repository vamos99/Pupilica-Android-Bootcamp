package com.example.bitirmeprojesi.utils

import android.content.Context
import androidx.lifecycle.lifecycleScope
import com.example.bitirmeprojesi.data.api.RetrofitClient
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.bitirmeprojesi.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response

object CartBadgeHelper {
    
    fun updateCartBadge(
        bottomNavigationView: BottomNavigationView,
        context: Context,
        scope: CoroutineScope
    ) {
        scope.launch {
            try {
                val prefs = context.getSharedPreferences("GetFoodPrefs", Context.MODE_PRIVATE)
                val isGuest = prefs.getBoolean("isGuest", true)
                
                if (isGuest) {
                    // Guest kullanıcı için badge'i gizle
                    val badge = bottomNavigationView.getOrCreateBadge(R.id.nav_cart)
                    badge.isVisible = false
                    return@launch
                }
                
                // API'ye her zaman "halil_kiyak" username'i ile istek gönder (rules.txt'e göre)
                val apiUsername = "halil_kiyak"
                val response = RetrofitClient.api.sepettekiYemekleriGetirRaw(apiUsername)
                val body = response.body()?.string()
                if (body.isNullOrBlank() || body.length < 10) {
                    // Sepet boşmuş gibi davran
                    val badge = bottomNavigationView.getOrCreateBadge(R.id.nav_cart)
                    badge.isVisible = false
                    return@launch
                }
                val sepetResponse = Gson().fromJson(body, com.example.bitirmeprojesi.data.model.SepetYemeklerResponse::class.java)
                val badge = bottomNavigationView.getOrCreateBadge(R.id.nav_cart)
                if (sepetResponse.success == 1 && sepetResponse.sepet_yemekler != null && sepetResponse.sepet_yemekler.isNotEmpty()) {
                    val totalItems = sepetResponse.sepet_yemekler.sumOf { it.yemek_siparis_adet.toInt() }
                    if (totalItems > 0) {
                        badge.number = totalItems
                        badge.isVisible = true
                        badge.backgroundColor = context.getColor(R.color.primary_mor)
                        badge.badgeTextColor = context.getColor(android.R.color.white)
                    } else {
                        badge.isVisible = false
                    }
                } else {
                    badge.isVisible = false
                }
                
            } catch (e: Exception) {
                android.util.Log.e("CartBadgeHelper", "Badge güncelleme hatası: ${e.message}", e)
                // Hata durumunda badge'i gizle
                val badge = bottomNavigationView.getOrCreateBadge(R.id.nav_cart)
                badge.isVisible = false
            }
        }
    }
}
