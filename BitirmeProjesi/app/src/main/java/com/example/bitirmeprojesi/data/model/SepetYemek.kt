package com.example.bitirmeprojesi.data.model

data class SepetYemek(
    val sepet_yemek_id: String,
    val yemek_adi: String,
    val yemek_resim_adi: String,
    val yemek_fiyat: String,
    val yemek_siparis_adet: String,
    val kullanici_adi: String
)

data class SepetYemeklerResponse(
    val sepet_yemekler: List<SepetYemek>?,
    val success: Int?
)

data class CrudResponse(
    val success: Int?,
    val message: String?
)