package com.example.bitirmeprojesi.ui.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bitirmeprojesi.data.entity.FavoriYemekler
import com.example.bitirmeprojesi.data.entity.Yemekler
import com.example.bitirmeprojesi.data.repo.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AnasayfaViewModel @Inject constructor(var yrepo: YemeklerRepository) : ViewModel(){
    var yemeklerListesi = MutableLiveData<List<Yemekler>>()
    var favoriYemeklerListesi = MutableLiveData<List<FavoriYemekler>>()

    init {
        yemekleriGetir()
    }

    fun yemekleriGetir(){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                yemeklerListesi.value = yrepo.yemekleriGetir()
            }
            catch (e: Exception){
                Log.e("hata",e.toString())
            }
        }
    }

    fun sepeteEkle(yemek_adi:String, yemek_resim_adi:String, yemek_fiyat:Int, yemek_siparis_adet:Int, kullanici_adi:String){
        CoroutineScope(Dispatchers.Main).launch {
            yrepo.sepeteEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
        }
    }

    //Favori listesine istenilen yemeği ekler
    fun favoriyeEkle(yemek_id:Int){
        var favoriYemekListesiKopya = favoriYemeklerListesi.value ?: mutableListOf()
        var favoriYemekListesiArray = favoriYemekListesiKopya.toMutableList()
        var yemekIdVarmi = false
        if(!favoriYemekListesiKopya.isEmpty()){
            for (i in favoriYemekListesiKopya) {
                if (i.yemek_id == yemek_id) {
                    yemekIdVarmi = true
                    break
                }
            }
            if (!yemekIdVarmi) {
                val yeniFavoriYemek = FavoriYemekler(yemek_id)
                favoriYemekListesiArray.add(yeniFavoriYemek)
                favoriYemeklerListesi.value = favoriYemekListesiArray
            }
        }else{
            val yeniFavoriYemek = FavoriYemekler(yemek_id)
            favoriYemekListesiArray.add(yeniFavoriYemek)
            favoriYemeklerListesi.value = favoriYemekListesiArray
        }
    }

    //Favori listesinden istenilen yemeği çıkarır
    fun favoridenCikar(yemek_id:Int){
        var favoriYemekListesiKopya = favoriYemeklerListesi.value ?: mutableListOf()
        var favoriYemekListesiArray = favoriYemekListesiKopya.toMutableList()
        if(!favoriYemekListesiKopya.isEmpty()){
            for (i in 0..favoriYemekListesiKopya.size-1) {
                if (favoriYemekListesiKopya[i].yemek_id == yemek_id) {
                    favoriYemekListesiArray.removeAt(i)
                    break
                }
            }
            favoriYemeklerListesi.value = favoriYemekListesiArray
        }
    }

    //Arama çubuğunda arama yaparken aranılan kelimeyi getirir
    fun yemekFiltrele(text:String) : ArrayList<Yemekler>{
        val filtrelenmisListe = ArrayList<Yemekler>()
        val yemeklerListesiKopya = yemeklerListesi.value ?: mutableListOf()
        for(item in yemeklerListesiKopya){
            if(item.yemek_adi.lowercase().contains(text.lowercase())){
                filtrelenmisListe.add(item)
            }
        }

        return filtrelenmisListe
    }


}




