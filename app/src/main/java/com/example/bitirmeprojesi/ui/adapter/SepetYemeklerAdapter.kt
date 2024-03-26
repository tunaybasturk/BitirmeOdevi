package com.example.bitirmeprojesi.ui.adapter

import android.content.Context
import android.icu.text.Transliterator.Position
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bitirmeprojesi.data.entity.SepetYemekler
import com.example.bitirmeprojesi.data.entity.Yemekler
import com.example.bitirmeprojesi.databinding.CardTasarimSepetYemeklerBinding
import com.example.bitirmeprojesi.databinding.CardTasarimYemeklerBinding
import com.example.bitirmeprojesi.ui.fragment.AnasayfaFragmentDirections
import com.example.bitirmeprojesi.ui.viewmodel.AnasayfaViewModel
import com.example.bitirmeprojesi.ui.viewmodel.SepetYemeklerViewModel

class SepetYemeklerAdapter (var mContext: Context,
                            var sepetYemeklerListesi:List<SepetYemekler>,
                            var viewModel: SepetYemeklerViewModel
)
    : RecyclerView.Adapter<SepetYemeklerAdapter.CardTasarimTutucu>() {
    inner class CardTasarimTutucu(var tasarim: CardTasarimSepetYemeklerBinding) : RecyclerView.ViewHolder(tasarim.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding = CardTasarimSepetYemeklerBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return CardTasarimTutucu(binding)
    }




    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val sepetYemek = sepetYemeklerListesi.get(position)
        val t = holder.tasarim
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${sepetYemek.yemek_resim_adi}"


        t.textViewSepetYemekAd.text = sepetYemek.yemek_adi
        t.textViewSepetYemekAdet.text = sepetYemek.yemek_siparis_adet
        t.textViewSepetYemekToplamFiyat.text = "₺ ${(sepetYemek.yemek_siparis_adet.toInt() * sepetYemek.yemek_fiyat.toInt())}"

        Glide.with(mContext).load(url).override(100,100).into(t.imageViewSepetYemek)

        //Silme butonuna tıklanılan yemeği siler
        t.buttonSepetYemekSil.setOnClickListener {
            val yemek_adi = sepetYemek.yemek_adi
            viewModel.sepettenYemekSil(yemek_adi)
        }

        //Bir adet yemek arttırır
        t.buttonSepetAdetArttir.setOnClickListener {
            var adet = t.textViewSepetYemekAdet.text.toString().toInt()
            if(adet>=1){
                adet+=1
                t.textViewSepetYemekAdet.text = adet.toString()
                t.textViewSepetYemekToplamFiyat.text = "₺ ${sepetYemek.yemek_fiyat.toInt() * adet}"
                viewModel.sepeteEkle(sepetYemek.yemek_adi,sepetYemek.yemek_resim_adi,sepetYemek.yemek_fiyat.toInt(),1,"tunay_basturk")
                viewModel.toplamFiyat.value = viewModel.toplamFiyat.value!! + sepetYemek.yemek_fiyat.toInt()
            }
        }

        //Bir adet yemek eksiltir
        t.buttonSepetAdetEksilt.setOnClickListener {
            var adet = t.textViewSepetYemekAdet.text.toString().toInt()
            if(adet>1){
                adet-=1
                t.textViewSepetYemekAdet.text = adet.toString()
                t.textViewSepetYemekToplamFiyat.text = "₺ ${sepetYemek.yemek_fiyat.toInt() * adet}"
                viewModel.sepettenAdetYemekSil(sepetYemek.yemek_adi)
                viewModel.toplamFiyat.value = viewModel.toplamFiyat.value!! - sepetYemek.yemek_fiyat.toInt()
            }
        }


    }

    override fun getItemCount(): Int {
        return sepetYemeklerListesi.size
    }
}