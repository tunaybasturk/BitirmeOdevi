package com.example.bitirmeprojesi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bitirmeprojesi.data.entity.FavoriYemekler
import com.example.bitirmeprojesi.data.entity.SepetYemekler
import com.example.bitirmeprojesi.data.entity.Yemekler
import com.example.bitirmeprojesi.data.repo.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriYemekViewModel @Inject constructor(var yrepo: YemeklerRepository) : ViewModel(){
    var yemeklerListesi = MutableLiveData<List<Yemekler>>()
    var favoriYemeklerListesi = MutableLiveData<List<FavoriYemekler>>()

    init {
        yemekleriGetir()
    }

    fun yemekleriGetir(){
        CoroutineScope(Dispatchers.Main).launch {
            yemeklerListesi.value = yrepo.yemekleriGetir()
        }
    }

    //Gönderilen favori yemek id'li yemekleri yemek listesinden alarak döndürür
    fun favoriYemekleriGetir(YemeklerListesi:List<Yemekler>,alinacakYemekIdler:ArrayList<Int>): List<Yemekler>{
        var YemeklerListesiKopya = YemeklerListesi.toMutableList()

        for(i in 0..YemeklerListesi.size-1){
            var kontrol = 0
            for(j in alinacakYemekIdler){
                if(YemeklerListesi[i].yemek_id == j){
                    kontrol = 1
                }
            }
            if(kontrol == 0){
                YemeklerListesiKopya.remove(YemeklerListesi[i])
            }
        }

        return YemeklerListesiKopya
    }

}