package com.revolve44.rickandmortycharacters.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.revolve44.rickandmortycharacters.MainActivity
import com.revolve44.rickandmortycharacters.R
import com.revolve44.rickandmortycharacters.adapter.MainScreenRecycleviewAdapter
import com.revolve44.rickandmortycharacters.base.ItemElementsDelegate
import com.revolve44.rickandmortycharacters.models.Character
import com.revolve44.rickandmortycharacters.utils.Resource
import com.revolve44.rickandmortycharacters.viewmodels.MainScreenViewModel
import timber.log.Timber


class MainScreenFragment : Fragment(R.layout.fragment_mainscreen) {

    private lateinit var mainScreenRecyclerView: RecyclerView
    private var mainScreenRecycleviewAdapter = MainScreenRecycleviewAdapter()
    private lateinit var mainscreenViewModel: MainScreenViewModel



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainScreenRecyclerView = view.findViewById(R.id.main_recycler_view)


        val activity = activity as Context
        mainscreenViewModel =(activity as MainActivity).mainScreenViewModel

        showRecyclerview()
        handleErrorsAndInternetConnection()
        getFirst15Elements()
    }

    private fun showRecyclerview() {
        mainScreenRecyclerView.adapter = mainScreenRecycleviewAdapter
        mainScreenRecyclerView.layoutManager = GridLayoutManager(activity, 2)
        mainScreenRecyclerView.setHasFixedSize(false)

        mainScreenRecycleviewAdapter.attachDelegate(object : ItemElementsDelegate<Character> {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onElementClick(model: Character, view: View,clickedPosition: Int) {

                Toast.makeText(activity, "Now is Pool element #$clickedPosition", Toast.LENGTH_SHORT).show()
                mainscreenViewModel.charactersListMain.value
                    ?.set(clickedPosition,Character(model.id,model.name,model.img_path,true))
                mainScreenRecycleviewAdapter.removeItemForPosition(clickedPosition)

            }
        })
    }


    private fun handleErrorsAndInternetConnection() {
        mainscreenViewModel.requestFor15charc.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {

                    response.data?.let { xmsresp ->
                        try {
                            Timber.i("vvv3 " + mainscreenViewModel.charactersListMain.value.toString())


                        } catch (e: Exception) {
                            Timber.e("error when showing recyclerview ${e.message}")
                        }
                    }
                }
                is Resource.Loading -> {
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "Loading...",
                        Snackbar.LENGTH_LONG
                    )
                }
                is Resource.Error -> {
                    noInternetAlert()
                    Timber.e("vvv Error load ${response.message}")
                }
            }
        })
    }

    private fun getFirst15Elements(){
        mainscreenViewModel.getAllCharacters().observe(viewLifecycleOwner, Observer {
                //noFilteredCharacters ->
            characters ->
//            var characters : MutableList<Character> = mutableListOf()
//
//            for (i in 0 .. noFilteredCharacters.size-1){
//
//                if (!noFilteredCharacters.get(i).in_pool){
//                    characters.add(noFilteredCharacters.get(i))
//                }
//            }
            Timber.i("vvv4 " + characters.toString())
            Timber.i("vvv5 " + characters.size + " size adapter " + mainScreenRecycleviewAdapter.sizeOfItems)

            if (characters.size < 16) {
                mainScreenRecycleviewAdapter.setList(characters)


            } else if (characters.size > mainScreenRecycleviewAdapter.sizeOfItems && mainScreenRecycleviewAdapter.hasItems) {
//                var pos = (0..characters.size - 1).random()
//                Timber.i("vvv6  added item " + pos)
                 Timber.i("vvv8 size adapter ${mainScreenRecycleviewAdapter.itemCount} size observ ${characters.size}")
                if (mainScreenRecycleviewAdapter.itemCount == 0){
                    mainScreenRecycleviewAdapter
                        .addItemToDefinePosition(characters.get(characters.size - 1),0)

                }else if (mainScreenRecycleviewAdapter.itemCount == 1){
                    mainScreenRecycleviewAdapter
                        .addItemToDefinePosition(characters.get(characters.size - 1), (0 until 1).random())

                }else{
                    mainScreenRecycleviewAdapter
                        .addItemToDefinePosition(characters.get(characters.size - 1), (0 until mainScreenRecycleviewAdapter.itemCount).random())
                }

            }else{
                mainScreenRecycleviewAdapter.updateItems(characters)
            }



        })
    }

    private fun noInternetAlert() {

        Snackbar.make(requireActivity().findViewById(android.R.id.content),"No Internet",Snackbar.LENGTH_LONG).show()

    }


}