package com.example.bitirmeprojesi.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.entity.FavoriYemekler
import com.example.bitirmeprojesi.data.entity.Yemekler
import com.example.bitirmeprojesi.databinding.FragmentAnasayfaBinding
import com.example.bitirmeprojesi.ui.adapter.YemeklerAdapter
import com.example.bitirmeprojesi.ui.viewmodel.AnasayfaViewModel
import com.example.bitirmeprojesi.ui.viewmodel.SepetYemeklerViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class AnasayfaFragment : Fragment() {
    private lateinit var binding: FragmentAnasayfaBinding
    private lateinit var anasayfaViewModel: AnasayfaViewModel
    private val sepetYemeklerViewModel: SepetYemeklerViewModel by activityViewModels()
    private lateinit  var yemeklerAdapter:YemeklerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnasayfaBinding.inflate(inflater, container, false)


        val args: AnasayfaFragmentArgs? = arguments?.let { AnasayfaFragmentArgs.fromBundle(it) }
        val gelenFavoriYemekListe = args?.favoriYemekListe
        var favoriYemekId = ""
        var favoriYemekListesiId = ArrayList<Int>()

        //Gelen listeyi "_"'dan ayrıştırma
        if (gelenFavoriYemekListe != null) {
            for(i in gelenFavoriYemekListe){
                if(i.toString() != "_"){
                    favoriYemekId+=i
                }else{
                    favoriYemekListesiId.add(favoriYemekId.toInt())
                    favoriYemekId = ""
                }
            }
        }

        //Gelen favori listesi kontrolü
        if(favoriYemekListesiId.size !=0){
            for(i in favoriYemekListesiId){
                anasayfaViewModel.favoriyeEkle(i)
            }
        }

        //Yemek arama
        binding.editTextYemekAra.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.editTextYemekAra.isFocusableInTouchMode = true
                binding.editTextYemekAra.requestFocus()
            }
        }

        //Favori yemek listesini getirme
        var favoriYemeklerListesi = ArrayList<FavoriYemekler>()
        anasayfaViewModel.favoriYemeklerListesi.observe(viewLifecycleOwner){
            if(!it.isEmpty()){
                favoriYemeklerListesi = ArrayList(it)
            }
        }


        anasayfaViewModel.yemeklerListesi.observe(viewLifecycleOwner){
            try {
                sepetYemeklerViewModel.sepettekiYemekleriGetir()
            }
            catch (e: Exception){}
                yemeklerAdapter = YemeklerAdapter(requireContext(),it,favoriYemeklerListesi,anasayfaViewModel)
                binding.yemeklerRv.adapter = yemeklerAdapter

        }

        //Yemek aranıldığında filtreleme yapma
        var filterenlemisYemeklerListesi = ArrayList<Yemekler>()
        binding.editTextYemekAra.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?,start: Int, before: Int, count: Int) {
                filterenlemisYemeklerListesi =  anasayfaViewModel.yemekFiltrele(s.toString())
                yemeklerAdapter.filtrelenmisliste(filterenlemisYemeklerListesi)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.yemeklerRv.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        binding.buttonSepet.setOnClickListener {
            val gecis = AnasayfaFragmentDirections.yemekSepetGecis()
            Navigation.findNavController(it).navigate(gecis)
        }

        //Anasayfadaki sağ üstteki sepetin üstünde bulunan ürün toplamı sayısını güncelleme
        sepetYemeklerViewModel.urunToplami.observe(viewLifecycleOwner) { urunToplami ->
            try {
                sepetYemeklerViewModel.sepettekiYemekleriGetir()
            }
            catch (e: Exception){}
            if(urunToplami > 99){
                binding.textViewSepetUrunSayisi.text = "99+"
            }else{
                binding.textViewSepetUrunSayisi.text = urunToplami.toString()
            }
        }

        //Bottom Navigation item seçme
        binding.bottomNavigationView.menu.findItem(R.id.home).isChecked = true
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorites -> {
                    var favoriYemeklerListeString = ""
                    if(anasayfaViewModel.favoriYemeklerListesi.value?.size !=0){
                        for(i in favoriYemeklerListesi){
                            favoriYemeklerListeString+=i.yemek_id.toString() + "_"
                        }
                    }

                    val navController = Navigation.findNavController(requireView())
                    val action = AnasayfaFragmentDirections.yemekFavoriGecis(favoriYemeklerListeString)
                    navController.navigate(action)
                    true
                }
                else -> true
            }
        }

        //Splash screen'e atmaması için anasayfada geri tuşuna basılırsa uygulamayı kapatma
        val geriTusu = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().finishAffinity()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,geriTusu)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: AnasayfaViewModel by viewModels()
        anasayfaViewModel = tempViewModel
    }



}