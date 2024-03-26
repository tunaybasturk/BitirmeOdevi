package com.example.bitirmeprojesi.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.entity.SepetYemekler
import com.example.bitirmeprojesi.databinding.FragmentSepetYemeklerBinding
import com.example.bitirmeprojesi.ui.adapter.SepetYemeklerAdapter
import com.example.bitirmeprojesi.ui.viewmodel.SepetYemeklerViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class SepetYemeklerFragment : Fragment() {
    private lateinit var binding: FragmentSepetYemeklerBinding
    val viewModel: SepetYemeklerViewModel by activityViewModels()
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = FragmentSepetYemeklerBinding.inflate(inflater, container, false)

         try {
             viewModel.sepettekiYemekleriGetir()
         }catch (e: Exception){}

         viewModel.sepetYemeklerListesi.observe(viewLifecycleOwner){
             val sepetYemeklerAdapter = SepetYemeklerAdapter(requireContext(),it,viewModel)
             binding.sepetYemeklerRv.adapter = sepetYemeklerAdapter
             binding.sepetYemeklerRv.itemAnimator = DefaultItemAnimator()
         }

         binding.sepetYemeklerRv.layoutManager = LinearLayoutManager(requireContext())

         viewModel.toplamFiyat.observe(viewLifecycleOwner) { toplam ->
             binding.textViewSepetYemeklerToplamFiyat.text = "₺${toplam}"
         }


         binding.buttonSepetiOnayla.setOnClickListener {
             viewModel.sepetiOnayla()
             val snackbar = Snackbar.make(binding.root, "Siparişiniz Alınmıştır.", Snackbar.LENGTH_LONG)
             snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bottom_nav_selected))
             val textView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
             textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
             snackbar.show()
         }




         return binding.root
    }




}