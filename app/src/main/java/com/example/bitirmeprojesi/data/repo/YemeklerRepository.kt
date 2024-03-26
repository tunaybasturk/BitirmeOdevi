package com.example.bitirmeprojesi.data.repo

import com.example.bitirmeprojesi.data.datasource.YemeklerDataSource
import com.example.bitirmeprojesi.data.entity.SepetYemekler
import com.example.bitirmeprojesi.data.entity.Yemekler

class YemeklerRepository (var yds: YemeklerDataSource){

    suspend fun yemekleriGetir() : List<Yemekler> = yds.yemekleriGetir()

    suspend fun sepeteEkle(yemek_adi:String, yemek_resim_adi:String, yemek_fiyat:Int, yemek_siparis_adet:Int, kullanici_adi:String) = yds.sepeteEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)

    suspend fun sepettekiYemekleriGetir(kullanici_adi:String) : List<SepetYemekler> = yds.sepettekiYemekleriGetir(kullanici_adi)

    suspend fun sepettenYemekSil(sepet_yemek_id:Int, kullanici_adi: String) = yds.sepettenYemekSil(sepet_yemek_id, kullanici_adi)
}