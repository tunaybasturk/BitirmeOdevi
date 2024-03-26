package com.example.bitirmeprojesi.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.entity.FavoriYemekler
import com.example.bitirmeprojesi.data.entity.Yemekler
import com.example.bitirmeprojesi.databinding.CardTasarimFavoriYemeklerBinding
import com.example.bitirmeprojesi.databinding.CardTasarimYemeklerBinding
import com.example.bitirmeprojesi.ui.fragment.AnasayfaFragmentDirections
import com.example.bitirmeprojesi.ui.viewmodel.AnasayfaViewModel
import com.example.bitirmeprojesi.ui.viewmodel.FavoriYemekViewModel
import kotlin.random.Random

class FavoriYemeklerAdapter(var mContext: Context,
                            var favoriYemeklerListesi:List<Yemekler>,
                            var viewModel: FavoriYemekViewModel
)
    : RecyclerView.Adapter<FavoriYemeklerAdapter.CardTasarimTutucu>() {
    inner class CardTasarimTutucu(var tasarim: CardTasarimFavoriYemeklerBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding = CardTasarimFavoriYemeklerBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardTasarimTutucu(binding)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val favoriYemek = favoriYemeklerListesi.get(position)
        val t = holder.tasarim
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${favoriYemek.yemek_resim_adi}"
        t.textViewYemekAd.text = favoriYemek.yemek_adi

        Glide.with(mContext).load(url).override(100,100).into(t.imageViewYemek)

        var yildizVeUzaklik = yildizVeUzaklik(favoriYemek.yemek_id)
        t.textViewYildizPuani.text = yildizVeUzaklik[0]
        t.textViewYemekUzaklik.text = yildizVeUzaklik[1]

        t.imageViewFavorilereEkle.setImageResource(R.drawable.favorites_selected)

    }

    override fun getItemCount(): Int {
        return favoriYemeklerListesi.size
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