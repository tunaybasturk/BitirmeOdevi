package com.example.bitirmeprojesi.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.databinding.FragmentYemeklerDetayBinding
import com.example.bitirmeprojesi.ui.viewmodel.YemekDetayViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YemeklerDetayFragment : Fragment() {
    private lateinit var binding: FragmentYemeklerDetayBinding
    private lateinit var viewModel: YemekDetayViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYemeklerDetayBinding.inflate(inflater, container, false)

        val bundle:YemeklerDetayFragmentArgs by navArgs()
        val gelenYemek = bundle.yemek
        val gelenKategoriId = bundle.kategoriId
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${gelenYemek.yemek_resim_adi}"
        var yildizVeUzaklik = viewModel.yildizVeUzaklik(gelenYemek.yemek_id)

        binding.textViewYemekDetayYildiz.text = yildizVeUzaklik[0]
        binding.textViewYemekDetayUzaklik.text = yildizVeUzaklik[1]

        //Kategroi renkleri için renkleri alma
        val selectedColor = ContextCompat.getColor(requireContext(), R.color.white)
        val notSelecetedColor = ContextCompat.getColor(requireContext(),R.color.bottom_nav_selected)

        //Gelen kategori numarasına göre gerekli UI ayarlamalarını yapma
        if(gelenKategoriId ==1){
            binding.imageViewKategoriYemek.setColorFilter(selectedColor)
            binding.imageViewKategoriIcecek.setColorFilter(notSelecetedColor)
            binding.imageViewKategoriTatli.setColorFilter(notSelecetedColor)
            binding.imageViewKategoriYemek.setBackgroundResource(R.drawable.imageview_kategori_selected_background)
            binding.imageViewKategoriIcecek.setBackgroundResource(R.drawable.imageview_kategori_not_selected_background)
            binding.imageViewKategoriTatli.setBackgroundResource(R.drawable.imageview_kategori_not_selected_background)
            binding.imageViewKategoriYemek.setPadding(50)
            binding.imageViewKategoriIcecek.setPadding(50)
            binding.imageViewKategoriTatli.setPadding(50)
        }else if(gelenKategoriId == 2){
            binding.imageViewKategoriYemek.setColorFilter(notSelecetedColor)
            binding.imageViewKategoriIcecek.setColorFilter(selectedColor)
            binding.imageViewKategoriTatli.setColorFilter(notSelecetedColor)
            binding.imageViewKategoriYemek.setBackgroundResource(R.drawable.imageview_kategori_not_selected_background)
            binding.imageViewKategoriIcecek.setBackgroundResource(R.drawable.imageview_kategori_selected_background)
            binding.imageViewKategoriTatli.setBackgroundResource(R.drawable.imageview_kategori_not_selected_background)
            binding.imageViewKategoriYemek.setPadding(50)
            binding.imageViewKategoriIcecek.setPadding(50)
            binding.imageViewKategoriTatli.setPadding(50)
        }else{
            binding.imageViewKategoriYemek.setColorFilter(notSelecetedColor)
            binding.imageViewKategoriIcecek.setColorFilter(notSelecetedColor)
            binding.imageViewKategoriTatli.setColorFilter(selectedColor)
            binding.imageViewKategoriYemek.setBackgroundResource(R.drawable.imageview_kategori_not_selected_background)
            binding.imageViewKategoriIcecek.setBackgroundResource(R.drawable.imageview_kategori_not_selected_background)
            binding.imageViewKategoriTatli.setBackgroundResource(R.drawable.imageview_kategori_selected_background)
            binding.imageViewKategoriYemek.setPadding(50)
            binding.imageViewKategoriIcecek.setPadding(50)
            binding.imageViewKategoriTatli.setPadding(50)
        }

        Glide.with(this).load(url).override(100,100).into(binding.imageViewYemekResim)
        binding.textViewYemekDetayAd.text = gelenYemek.yemek_adi
        var yemekDetayAdetFiyat= gelenYemek.yemek_fiyat
        binding.textViewToplamFiyat.text = "₺ ${gelenYemek.yemek_fiyat}"
        var yeniAdet=1

        //Yemek adet arttırma
        binding.buttonAdetArttir.setOnClickListener {
            var adet = binding.textViewYemekDetayAdet.text.toString().toInt()
            yeniAdet = adet + 1
            binding.textViewYemekDetayAdet.text = yeniAdet.toString()
            val yeniFiyat = yeniAdet * yemekDetayAdetFiyat
            binding.textViewToplamFiyat.text = "₺ ${yeniFiyat}"
        }

        //Yemek adet eksiltme
        binding.buttonAdetEksilt.setOnClickListener {
            val adet = binding.textViewYemekDetayAdet.text.toString().toInt()
            if(adet>=1){
                yeniAdet = adet - 1
                if(yeniAdet>0){
                    binding.textViewYemekDetayAdet.text = yeniAdet.toString()
                    val yeniFiyat = yeniAdet * yemekDetayAdetFiyat
                    binding.textViewToplamFiyat.text = "₺ ${yeniFiyat}"
                }
            }
        }

        //İstenilen adet ile sepete yemek ekleme ve bilgi amaçlı snackbar gösterimi
        binding.buttonSepetEkle.setOnClickListener {
            val yemek_adi = gelenYemek.yemek_adi
            val yemek_resim_adi  = gelenYemek.yemek_resim_adi
            val yemek_fiyat = gelenYemek.yemek_fiyat
            val yemek_siparis_adet = yeniAdet
            val kullanici_adi = "tunay_basturk"
            viewModel.sepeteEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
            val snackbar = Snackbar.make(binding.root, "${yemek_siparis_adet} Adet ${gelenYemek.yemek_adi} Sepetinize Eklenmiştir.", Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.bottom_nav_selected))
            val textView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            snackbar.show()
        }

        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: YemekDetayViewModel by viewModels()
        viewModel = tempViewModel
    }


}