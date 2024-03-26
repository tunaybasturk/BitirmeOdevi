package com.example.bitirmeprojesi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.bitirmeprojesi.data.repo.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YemekDetayViewModel @Inject constructor(var yrepo:YemeklerRepository) : ViewModel(){

    fun sepeteEkle(yemek_adi:String, yemek_resim_adi:String, yemek_fiyat:Int, yemek_siparis_adet:Int, kullanici_adi:String){
        CoroutineScope(Dispatchers.Main).launch {
            yrepo.sepeteEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
        }
    }

    //Sabit yıldız ve uzaklık sayısı(Veri tabanı olmadığından kendimce değerler verdim)
    fun yildizVeUzaklik(yemekId:Int):ArrayList<String>{
        var yildizVeUzaklik = ArrayList<String>()
        if(yemekId % 5 ==0){
            yildizVeUzaklik.add("4.4")
            yildizVeUzaklik.add("1.5 km")
        }else if(yemekId % 7 == 0){
            yildizVeUzaklik.add("4.8")
            yildizVeUzaklik.add("0.8 km")
        }else if(yemekId % 6 == 0){
            yildizVeUzaklik.add("4.2")
            yildizVeUzaklik.add("1.2 km")
        }else if(yemekId % 8 == 0){
            yildizVeUzaklik.add("3.9")
            yildizVeUzaklik.add("0.5 km")
        }else if(yemekId % 9 == 0){
            yildizVeUzaklik.add("3.5")
            yildizVeUzaklik.add("0.9 km")
        }else if(yemekId % 11 == 0){
            yildizVeUzaklik.add("4.9")
            yildizVeUzaklik.add("1.2 km")
        }else if(yemekId % 13 == 0){
            yildizVeUzaklik.add("4.7")
            yildizVeUzaklik.add("1.1 km")
        }else if(yemekId == 4){
            yildizVeUzaklik.add("4.1")
            yildizVeUzaklik.add("0.4 km")
        }
        else if(yemekId == 3){
            yildizVeUzaklik.add("4.6")
            yildizVeUzaklik.add("1.0 km")
        }
        else if(yemekId == 2){
            yildizVeUzaklik.add("3.8")
            yildizVeUzaklik.add("2.3 km")
        }else{
            yildizVeUzaklik.add("4.8")
            yildizVeUzaklik.add("0.1 km")
        }

        return yildizVeUzaklik
    }

}