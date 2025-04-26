package com.example.androidbootcamp.odev2

class Odev2Set1Hesaplamalar {

    fun dereceToFahrenheit(celsius: Double): Double {
        return celsius * 1.8 + 32
    }

    fun dikdortgenCevre(kisaKenar: Double, uzunKenar: Double): Double {
        return if (kisaKenar > 0 && uzunKenar > 0) {
            2 * (kisaKenar + uzunKenar)
        } else {
            println("Hata: Dikdörtgen kenarları pozitif olmalıdır.")
            0.0
        }
    }

    fun faktoriyelHesapla(n: Int): Long {
        if (n < 0) {
            println("Hata: Faktöriyel negatif sayılar için hesaplanamaz.")
            return -1L
        }
        if (n == 0) {
            return 1L
        }
        var sonuc: Long = 1
        for (i in 1..n) {
            try {
                // Büyük sayılarda taşmayı önlemek için güvenli çarpma
                sonuc = Math.multiplyExact(sonuc, i.toLong())
            } catch (e: ArithmeticException) {
                println("Hata: Faktöriyel hesaplaması Long tipinin sınırlarını aştı.")
                return -2L // Taşma hatası kodu
            }
        }
        return sonuc
    }

    fun aHarfiSayisiniBul(kelime: String): Int {
        var sayac = 0
        for (harf in kelime) {
            if (harf.equals('a', ignoreCase = true)) {
                sayac++
            }
        }
        return sayac
    }
}