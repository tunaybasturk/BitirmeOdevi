package com.example.bitirmeprojesi.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.entity.FavoriYemekler
import com.example.bitirmeprojesi.data.entity.Yemekler
import com.example.bitirmeprojesi.databinding.FragmentFavoriYemeklerBinding
import com.example.bitirmeprojesi.ui.adapter.FavoriYemeklerAdapter
import com.example.bitirmeprojesi.ui.adapter.YemeklerAdapter
import com.example.bitirmeprojesi.ui.viewmodel.AnasayfaViewModel
import com.example.bitirmeprojesi.ui.viewmodel.FavoriYemekViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriYemeklerFragment : Fragment() {
    private lateinit var binding: FragmentFavoriYemeklerBinding
    private lateinit var viewModel : FavoriYemekViewModel
    val anasayfaViewModel: AnasayfaViewModel by  activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriYemeklerBinding.inflate(inflater, container, false)


        val bundle: FavoriYemeklerFragmentArgs by navArgs()
        var gelenFavoriYemekListe = bundle.favoriYemekListesi
        var favoriYemekListesiId = ArrayList<Int>()

        //Gelen listeyi "_"'dan ayrıştırma
        var favoriYemekId = ""
        for(i in gelenFavoriYemekListe){
            if(i.toString() != "_"){
                favoriYemekId+=i
            }else{
                favoriYemekListesiId.add(favoriYemekId.toInt())
                favoriYemekId = ""
            }
        }



        //Favori yemekleri listeleme
        viewModel.yemeklerListesi.observe(viewLifecycleOwner){
            var favoriYemeklerListesi = viewModel.favoriYemekleriGetir(it,favoriYemekListesiId)
            val FavoriyemeklerAdapter = FavoriYemeklerAdapter(requireContext(),favoriYemeklerListesi,viewModel)
            binding.favoriYemeklerRv.adapter = FavoriyemeklerAdapter
        }

        binding.favoriYemeklerRv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        //Bottom Navigation item seçme
        binding.bottomNavigationView.menu.findItem(R.id.favorites).isChecked = true
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    val navController = Navigation.findNavController(requireView())
                    for (i in favoriYemekListesiId){
                        anasayfaViewModel.favoriyeEkle(i)
                    }
                    val action = FavoriYemeklerFragmentDirections.anasayfaGecis(favoriYemekListe = gelenFavoriYemekListe)
                    navController.navigate(action)
                    true
                }
                else -> true
            }
        }

        binding.buttonSepet.setOnClickListener {
            val gecis = FavoriYemeklerFragmentDirections.yemekSepetGecis2()
            Navigation.findNavController(it).navigate(gecis)
        }

        return binding.root
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: FavoriYemekViewModel by viewModels()
        viewModel = tempViewModel
    }


}