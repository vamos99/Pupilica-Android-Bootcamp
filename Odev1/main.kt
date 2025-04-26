package gun2
import java.time.LocalDate

data class KisiBilgileri(
    val musteriAdi: String,
    val dogumGunu: LocalDate,
    val meslek: String,
    val maas: Double,
    val medeniDurum: String,
    val email: String,
    val telefon: String
)

data class AdresBilgileri(
    val sehir: String,
    val ulke: String,
    val postaKodu: String,
    val sokakAdi: String,
    val faturaAdresi: String
)

data class UrunBilgileri(
    val urunAdi: String,
    val urunYorum: String,
    val urunPuani: Double,
    val stokMiktari: Int,
    val indirimMiktari: Double,
    val yemekFiyati: Double
)

data class OdemeBilgileri(
    val odemeTarihi: LocalDate,
    val odeme: Boolean,
    val bakiye: Double,
    val kuponKodu: String,
    val kuponSuresi: Int,
    val takipKodu: String
)

data class MedyaBilgileri(
    val kitapAdi: String,
    val yayinlamaTarihi: LocalDate,
    val muzikAdi: String,
    val videoSuresi: Int,
    val resimAdi: String,
    val dosyaFormati: String
)

data class TeknolojiBilgileri(
    val marka: String,
    val telefonModeli: String,
    val ekranBoyutu: Double,
    val agirlik: Double,
    val arabaModeli: String,
    val renk: String,
    val renkKodu: String
)

data class RezervasyonBilgileri(
    val rezervasyonTarihi: LocalDate,
    val odaSayisi: Int,
    val kalanDakika: Int,
    val otobusHatti: String,
    val ulusalGun: String,
    val tatilGunu: String
)

data class Lokasyon(
    val enlem: Double,
    val boylam: Double
)

fun main() {
    val kisi = KisiBilgileri("Halil Kıyak", LocalDate.of(1995, 5, 20), "Yazılım Geliştirici", 25000.0, "Bekar", "ornek@eposta.com", "+905551112233")
    val adres = AdresBilgileri("İstanbul", "Türkiye", "34000", "Bağdat Caddesi", "Merkez Mah. 123. Sk. No:5, İstanbul")
    val urun = UrunBilgileri("Akıllı Saat", "Harika bir ürün!", 4.7, 120, 25.5, 89.99)
    val odeme = OdemeBilgileri(LocalDate.now(), true, 1500.75, "INDIRIM25", 30, "TR123456789")
    val medya = MedyaBilgileri("Kotlin ile Programlama", LocalDate.of(2021, 9, 15), "Imagine", 215, "profil.png", "PDF")
    val teknoloji = TeknolojiBilgileri("Samsung", "iPhone 14", 6.1, 1.2, "Toyota Corolla", "Kırmızı", "#FF0000")
    val rezervasyon = RezervasyonBilgileri(LocalDate.of(2025, 6, 10), 2, 7, "34AS", "29 Ekim Cumhuriyet Bayramı", "1 Ocak")
    val lokasyon = Lokasyon(41.0082, 28.9784)

    println(kisi)
    println(adres)
    println(urun)
    println(odeme)
    println(medya)
    println(teknoloji)
    println(rezervasyon)
    println(lokasyon)
}