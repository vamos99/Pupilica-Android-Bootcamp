package com.example.bitirmeprojesi.data.model

data class Yemek(
    val yemek_id: String,
    val yemek_adi: String,
    val yemek_resim_adi: String,
    val yemek_fiyat: String
)

data class YemeklerResponse(
    val yemekler: List<Yemek>,
    val success: Int
)