package com.example.bitirmeprojesi.data.datasource

import com.example.bitirmeprojesi.data.entity.SepetYemekler
import com.example.bitirmeprojesi.data.entity.Yemekler
import com.example.bitirmeprojesi.retrofit.YemeklerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YemeklerDataSource(var ydao:YemeklerDao) {

    suspend fun yemekleriGetir() : List<Yemekler> =
        withContext(Dispatchers.IO){
            return@withContext ydao.yemekleriGetir().yemekler
        }

    suspend fun sepeteEkle(yemek_adi:String, yemek_resim_adi:String, yemek_fiyat:Int, yemek_siparis_adet:Int, kullanici_adi:String) = ydao.sepeteEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)

    suspend fun sepettekiYemekleriGetir(kullanici_adi:String) :List<SepetYemekler> =
        withContext(Dispatchers.IO){
            return@withContext ydao.sepettekiYemekleriGetir(kullanici_adi).sepet_yemekler
        }

    suspend fun sepettenYemekSil(sepet_yemek_id:Int, kullanici_adi: String) = ydao.sepettenYemekSil(sepet_yemek_id, kullanici_adi)

}