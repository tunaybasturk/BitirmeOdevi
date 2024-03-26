package com.example.bitirmeprojesi.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.entity.FavoriYemekler
import com.example.bitirmeprojesi.data.entity.Yemekler
import com.example.bitirmeprojesi.databinding.CardTasarimYemeklerBinding
import com.example.bitirmeprojesi.ui.fragment.AnasayfaFragmentDirections
import com.example.bitirmeprojesi.ui.viewmodel.AnasayfaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class YemeklerAdapter(var mContext: Context,
                      var yemeklerListesi:List<Yemekler>,
                      var favoriYemeklerListesi:List<FavoriYemekler>,
                      var viewModel: AnasayfaViewModel)
    : RecyclerView.Adapter<YemeklerAdapter.CardTasarimTutucu>() {
    inner class CardTasarimTutucu(var tasarim:CardTasarimYemeklerBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding = CardTasarimYemeklerBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardTasarimTutucu(binding)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val yemek = yemeklerListesi.get(position)
        val t = holder.tasarim
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}"
        t.textViewYemekAd.text = yemek.yemek_adi
        t.textViewYemekFiyat.text = "₺ ${yemek.yemek_fiyat}"

        Glide.with(mContext).load(url).override(100,100).into(t.imageViewYemek)

        //Ürünlerin apideki veri tabanında kategorisi olmadığından manuel şekilde kategorileme yapma
        t.cardViewYemek.setOnClickListener {
            if(yemek.yemek_adi == "Ayran" || yemek.yemek_adi == "Fanta" || yemek.yemek_adi == "Kahve" || yemek.yemek_adi == "Su"){
               val gecis = AnasayfaFragmentDirections.yemekDetayGecis(yemek = yemek,2)
                Navigation.findNavController(it).navigate(gecis)
            }
            else if(yemek.yemek_adi == "Baklava" || yemek.yemek_adi == "Kadayıf" || yemek.yemek_adi == "Sütlaç" || yemek.yemek_adi == "Tiramisu"){
              val gecis = AnasayfaFragmentDirections.yemekDetayGecis(yemek = yemek,3)
                Navigation.findNavController(it).navigate(gecis)
            }else{
                val gecis = AnasayfaFragmentDirections.yemekDetayGecis(yemek = yemek,1)
                Navigation.findNavController(it).navigate(gecis)
            }
        }

        var yildizVeUzaklik = yildizVeUzaklik(yemek.yemek_id)
        t.textViewYildizPuani.text = yildizVeUzaklik[0]
        t.textViewYemekUzaklik.text = yildizVeUzaklik[1]

        //Sepete ekler ve bilgi amaçlı ekranda snackbar gösterir
        t.sepeteEkle.setOnClickListener {
            try{
                viewModel.sepeteEkle(yemek.yemek_adi, yemek.yemek_resim_adi, yemek.yemek_fiyat, 1, "tunay_basturk")
            } catch (e: Exception){}
            val snackbar = Snackbar.make(it, "1 Adet ${yemek.yemek_adi} Sepetinize Eklenmiştir.", Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(mContext,R.color.bottom_nav_selected))
            val textView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            snackbar.show()
        }

        var favorideVarmi = false

        //Favori yemek listesi kontrolü
        if(favoriYemeklerListesi.isNotEmpty()) {
            for(i in favoriYemeklerListesi){
                if(yemeklerListesi[position].yemek_id == i.yemek_id){
                    favorideVarmi  = true
                    break
                }
            }
        }

        if (favorideVarmi) {
            t.imageViewFavorilereEkle.setImageResource(R.drawable.favorites_selected)
        } else {
            t.imageViewFavorilereEkle.setImageResource(R.drawable.favorites_not_selected)
        }

        //Eğer favoride varsa veya yoksa ona göre favori butonunun resmini değiştirir
        t.imageViewFavorilereEkle.setOnClickListener {
            if(!favorideVarmi) {
                viewModel.favoriyeEkle(yemek.yemek_id)
                t.imageViewFavorilereEkle.setImageResource(R.drawable.favorites_selected)
                favorideVarmi  = true
            } else {
                viewModel.favoridenCikar(yemek.yemek_id)
                t.imageViewFavorilereEkle.setImageResource(R.drawable.favorites_not_selected)
                favorideVarmi  = false
            }
        }

    }

    override fun getItemCount(): Int {
        return yemeklerListesi.size
    }

    //Arama esnasındaki yazılan text'e göre filtrelenmiş listeyi recyclerView'da gösterir
    fun filtrelenmisliste(filtrelenmisListe :ArrayList<Yemekler>){
        this.yemeklerListesi = filtrelenmisListe
        notifyDataSetChanged()
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