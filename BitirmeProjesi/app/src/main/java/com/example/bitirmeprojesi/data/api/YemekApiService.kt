package com.example.bitirmeprojesi.data.api

import com.example.bitirmeprojesi.data.model.CrudResponse
import com.example.bitirmeprojesi.data.model.SepetYemeklerResponse
import com.example.bitirmeprojesi.data.model.YemeklerResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import okhttp3.ResponseBody
import retrofit2.Response

interface YemekApiService {
    
    @GET("tumYemekleriGetir.php")
    suspend fun tumYemekleriGetir(): YemeklerResponse
    
    @POST("sepeteYemekEkle.php")
    @FormUrlEncoded
    suspend fun sepeteYemekEkle(
        @Field("yemek_adi") yemekAdi: String,
        @Field("yemek_resim_adi") yemekResimAdi: String,
        @Field("yemek_fiyat") yemekFiyat: String,
        @Field("yemek_siparis_adet") yemekSiparisAdet: String,
        @Field("kullanici_adi") kullaniciAdi: String
    ): CrudResponse
    
    @POST("sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun sepettekiYemekleriGetir(
        @Field("kullanici_adi") kullaniciAdi: String
    ): SepetYemeklerResponse
    
    @POST("sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun sepettekiYemekleriGetirRaw(
        @Field("kullanici_adi") kullaniciAdi: String
    ): Response<ResponseBody>
    
    @POST("sepettenYemekSil.php")
    @FormUrlEncoded
    suspend fun sepettenYemekSil(
        @Field("sepet_yemek_id") sepetYemekId: String,
        @Field("kullanici_adi") kullaniciAdi: String
    ): CrudResponse
}