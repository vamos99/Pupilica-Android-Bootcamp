package com.example.androidbootcamp.odev2

import kotlin.math.ceil // Kota aşımı için gerekli

class Odev2Set2Hesaplamalar {

    fun icAcilarToplami(kenarSayisi: Int): Int {
        return if (kenarSayisi >= 3) {
            (kenarSayisi - 2) * 180
        } else {
            println("Hata: Çokgenin en az 3 kenarı olmalıdır.")
            -1
        }
    }

    fun maasHesapla(gunSayisi: Int): Double {
        if (gunSayisi < 0) {
            println("Hata: Gün sayısı negatif olamaz.")
            return 0.0
        }

        // Maaş hesaplama sabitleri
        val saatUcretiNormal = 10.0
        val saatUcretiMesai = 20.0
        val gunlukCalismaSaati = 8
        val mesaiSiniriSaat = 160

        val toplamCalismaSaati = gunSayisi * gunlukCalismaSaati
        val maas: Double

        if (toplamCalismaSaati <= mesaiSiniriSaat) {
            maas = toplamCalismaSaati * saatUcretiNormal
        } else {
            val normalCalismaSaati = mesaiSiniriSaat
            val mesaiSaati = toplamCalismaSaati - mesaiSiniriSaat
            maas = (normalCalismaSaati * saatUcretiNormal) + (mesaiSaati * saatUcretiMesai)
        }
        return maas
    }

    fun internetFaturasiHesapla(kullanilanGB: Double): Double {
        if (kullanilanGB < 0) {
            println("Hata: Kullanılan GB miktarı negatif olamaz.")
            return 0.0
        }

        // Fatura hesaplama sabitleri
        val kotaSiniriGB = 50.0
        val kotaUcreti = 100.0
        val kotaAsimUcretiGBBasina = 4.0

        val toplamUcret: Double

        if (kullanilanGB <= kotaSiniriGB) {
            toplamUcret = kotaUcreti
        } else {
            // Aşılan GB miktarını yukarı yuvarla (örn: 0.1GB aşım 1GB olarak ücretlenir)
            val asilanMiktarGB = ceil(kullanilanGB - kotaSiniriGB)
            val asimUcreti = asilanMiktarGB * kotaAsimUcretiGBBasina
            toplamUcret = kotaUcreti + asimUcreti
        }
        return toplamUcret
    }
}