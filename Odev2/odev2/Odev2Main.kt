package com.example.androidbootcamp.odev2

fun main() {
    // Hesaplama class'larından nesneler oluşturuluyor
    val set1Hesaplayici = Odev2Set1Hesaplamalar()
    val set2Hesaplayici = Odev2Set2Hesaplamalar()

    println("--- ÖDEV 2 (OOP) TESTLERİ ---")

    println("\n--- Set 1 Testleri ---")

    // Test 1: Derece -> Fahrenheit
    val derece = 28.5
    val fahrenheit = set1Hesaplayici.dereceToFahrenheit(derece)
    println("1. $derece°C = $fahrenheit°F")

    // Test 2: Dikdörtgen Çevre
    val kisa = 7.0
    val uzun = 12.0
    val cevre = set1Hesaplayici.dikdortgenCevre(kisa, uzun)
    println("2. Kenarları $kisa ve $uzun olan dikdörtgenin çevresi: $cevre")
    set1Hesaplayici.dikdortgenCevre(-5.0, 10.0) // Hata testi: Negatif kenar

    // Test 3: Faktöriyel
    val sayi = 7
    val faktoriyel = set1Hesaplayici.faktoriyelHesapla(sayi)
    println("3. $sayi! = $faktoriyel")
    println("   0! = ${set1Hesaplayici.faktoriyelHesapla(0)}") // Sıfır faktöriyel testi
    set1Hesaplayici.faktoriyelHesapla(-4) // Hata testi: Negatif sayı

    // Test 4: 'a' Harfi Sayısı
    val kelime = "Adapazarı ve Ankara"
    val aSayisi = set1Hesaplayici.aHarfiSayisiniBul(kelime)
    println("4. '$kelime' kelimesindeki 'a'/'A' harfi sayısı: $aSayisi")

    println("\n--- Set 2 Testleri ---")

    // Test 1: İç Açılar Toplamı
    val kenar = 8
    val aciToplam = set2Hesaplayici.icAcilarToplami(kenar)
    println("1. $kenar kenarlı çokgenin iç açıları toplamı: $aciToplam derece")
    set2Hesaplayici.icAcilarToplami(2) // Hata testi: Geçersiz kenar sayısı

    // Test 2: Maaş Hesaplama
    val calisilanGun1 = 20
    val maas1 = set2Hesaplayici.maasHesapla(calisilanGun1)
    println("2. $calisilanGun1 gün çalışma maaşı (tam sınır): $maas1 ₺") // Beklenen: 1600.0

    val calisilanGun2 = 23
    val maas2 = set2Hesaplayici.maasHesapla(calisilanGun2)
    println("   $calisilanGun2 gün çalışma maaşı (mesaili): $maas2 ₺") // Beklenen: 2080.0
    set2Hesaplayici.maasHesapla(-5) // Hata testi: Negatif gün

    // Test 3: İnternet Faturası Hesaplama
    val kotaKullanim1 = 49.9
    val ucret1 = set2Hesaplayici.internetFaturasiHesapla(kotaKullanim1)
    println("3. $kotaKullanim1 GB kullanım ücreti (kota aşılmadı): $ucret1 ₺") // Beklenen: 100.0

    val kotaKullanim2 = 50.1
    val ucret2 = set2Hesaplayici.internetFaturasiHesapla(kotaKullanim2)
    println("   $kotaKullanim2 GB kullanım ücreti (az aşım): $ucret2 ₺") // Beklenen: 104.0

    val kotaKullanim3 = 70.0
    val ucret3 = set2Hesaplayici.internetFaturasiHesapla(kotaKullanim3)
    println("   $kotaKullanim3 GB kullanım ücreti (çok aşım): $ucret3 ₺") // Beklenen: 180.0
    set2Hesaplayici.internetFaturasiHesapla(-10.0) // Hata testi: Negatif GB

    println("\n--- TAMAMLANDI ---")
}