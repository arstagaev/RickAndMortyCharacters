package com.revolve44.rickandmortycharacters.ui

import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.revolve44.rickandmortycharacters.MainActivity
import com.revolve44.rickandmortycharacters.R
import com.revolve44.rickandmortycharacters.adapter.MainScreenRecycleviewAdapter
import com.revolve44.rickandmortycharacters.utils.Resource
import com.revolve44.rickandmortycharacters.viewmodels.MainScreenViewModel
import timber.log.Timber


class MainScreenFragment : Fragment(R.layout.fragment_mainscreen) {

    lateinit var mainScreenRecyclerView: RecyclerView
    var mainScreenRecycleviewAdapter = MainScreenRecycleviewAdapter()
    lateinit var mainscreenViewModel: MainScreenViewModel



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainScreenRecyclerView = view.findViewById(R.id.main_recycler_view)


        val activity = activity as Context
        mainscreenViewModel =(activity as MainActivity).mainScreenViewModel


        mainScreenRecyclerView.adapter = mainScreenRecycleviewAdapter
        mainScreenRecyclerView.layoutManager = GridLayoutManager(activity, 2)
        mainScreenRecyclerView.setHasFixedSize(false)

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
                        activity.findViewById(android.R.id.content),
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

        var chr = 1

        mainscreenViewModel.getAllCharacters().observe(viewLifecycleOwner, Observer { characters ->
            Timber.i("vvv4 " + characters.toString())
            Timber.i("vvv5 " + characters.size + " size adapter " + mainScreenRecycleviewAdapter.sizeOfItems)

            if (characters.size < 16) {
                mainScreenRecycleviewAdapter.setList(characters)
                chr = 0

            } else if (characters.size > mainScreenRecycleviewAdapter.sizeOfItems && mainScreenRecycleviewAdapter.hasItems) {
                var pos = (0..characters.size - 1).random()
                Timber.i("vvv6  added item " + pos)

                mainScreenRecycleviewAdapter
                    .addItemToDefinePosition(characters.get(characters.size - 1), pos)

            } else {

                mainScreenRecycleviewAdapter.updateItems(characters)
            }


        })

    }

    private fun noInternetAlert() {

        Snackbar.make(requireActivity().findViewById(android.R.id.content),"No Internet",Snackbar.LENGTH_LONG).show()

    }


}