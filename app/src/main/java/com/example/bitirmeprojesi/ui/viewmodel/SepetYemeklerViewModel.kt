package com.example.bitirmeprojesi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bitirmeprojesi.data.entity.SepetYemekler
import com.example.bitirmeprojesi.data.entity.Yemekler
import com.example.bitirmeprojesi.data.repo.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SepetYemeklerViewModel @Inject constructor(var yrepo: YemeklerRepository) : ViewModel(){
    var sepetYemeklerListesi = MutableLiveData<List<SepetYemekler>>()
    var toplamFiyat = MutableLiveData<Int>()
    var urunToplami = MutableLiveData<Int>()



    fun sepettekiYemekleriGetir(){
        CoroutineScope(Dispatchers.Main).launch {
            val kullanici_adi = "tunay_basturk"

            try {
                val guncellenecekSepetYemeklistesi = yrepo.sepettekiYemekleriGetir(kullanici_adi)
                withContext(Dispatchers.Main) {
                if (guncellenecekSepetYemeklistesi.isNotEmpty()) {
                    val guncellenmisListe = listeyiGuncelle(guncellenecekSepetYemeklistesi)
                    sepetYemeklerListesi.value = guncellenmisListe
                    toplamFiyat.value =
                        guncellenmisListe.sumOf { it.yemek_fiyat.toInt() * it.yemek_siparis_adet.toInt() }
                    urunToplami.value = guncellenmisListe.sumOf { it.yemek_siparis_adet.toInt() }
                } else {
                    sepetYemeklerListesi.value = emptyList()
                    toplamFiyat.value = 0
                    urunToplami.value = 0
                }
            }
            } catch (e:Exception){
                sepetYemeklerListesi.value = emptyList()
                toplamFiyat.value = 0
                urunToplami.value = 0
            }

        }
    }

   //Sepetten o yemeği siler
   fun sepettenYemekSil(yemekAdi: String){
       CoroutineScope(Dispatchers.Main).launch {

           val sepetListesi = yrepo.sepettekiYemekleriGetir("tunay_basturk")


           val silinecekIdler = sepetListesi.filter { it.yemek_adi == yemekAdi }.map { it.sepet_yemek_id.toInt() }

           val silmeIslemleri = silinecekIdler.map { id ->
               async(Dispatchers.Main) {
                   try {
                       yrepo.sepettenYemekSil(id,"tunay_basturk")
                   } catch (e: Exception) {

                   }
               }
           }

           silmeIslemleri.awaitAll()

           sepettekiYemekleriGetir()
       }

   }

    //Sepetten bir adet yemek siler
    fun sepettenAdetYemekSil(yemekAdi: String){
        CoroutineScope(Dispatchers.Main).launch {

            val sepetListesi = yrepo.sepettekiYemekleriGetir("tunay_basturk")


            val silinecekId = sepetListesi.firstOrNull { it.yemek_adi == yemekAdi }?.sepet_yemek_id?.toInt()

            if(silinecekId != null){
                yrepo.sepettenYemekSil(silinecekId,"tunay_basturk")
            }

        }

    }

    //Tüm sepetteki ürünleri siler
    fun sepetiOnayla(){
        CoroutineScope(Dispatchers.Main).launch {

            val sepetListesi = yrepo.sepettekiYemekleriGetir("tunay_basturk")


            val silinecekIdler = sepetListesi.map { it.sepet_yemek_id.toInt() }

            val silmeIslemleri = silinecekIdler.map { id ->
                async(Dispatchers.Main) {
                    try {
                        yrepo.sepettenYemekSil(id,"tunay_basturk")
                    } catch (e: Exception) {

                    }
                }
            }

            silmeIslemleri.awaitAll()

            sepettekiYemekleriGetir()
        }
    }

    fun sepeteEkle(yemek_adi:String, yemek_resim_adi:String, yemek_fiyat:Int, yemek_siparis_adet:Int, kullanici_adi:String){
        CoroutineScope(Dispatchers.Main).launch {
            yrepo.sepeteEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
        }
    }

    //Ürünlerin isimlerine göre yeni bir listede adetleriyle toplayarak birleştirip yeni bir liste oluşturur
    private fun listeyiGuncelle(sepetYemeklerListesi: List<SepetYemekler>): List<SepetYemekler> {
        var sepetYemeklerListesiKopya = sepetYemeklerListesi.toMutableList()
        for (i in 0..sepetYemeklerListesiKopya.size-1){
            var adet = sepetYemeklerListesiKopya[i].yemek_siparis_adet.toInt()
            var cikarilacakIndex = ArrayList<Int>()

            for(j in i+1..sepetYemeklerListesiKopya.size-1){
                if(sepetYemeklerListesiKopya[i].yemek_adi == sepetYemeklerListesiKopya[j].yemek_adi){
                    adet += sepetYemeklerListesiKopya[j].yemek_siparis_adet.toInt()
                    cikarilacakIndex.add(j)
                }
            }

            for(k in cikarilacakIndex.size-1 downTo 0){
                sepetYemeklerListesiKopya.removeAt(cikarilacakIndex[k])
            }

            sepetYemeklerListesiKopya[i].yemek_siparis_adet = adet.toString()

            if(sepetYemeklerListesiKopya.size == i+1){
                break
            }
        }

        return sepetYemeklerListesiKopya
    }


}