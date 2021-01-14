package com.revolve44.rickandmortycharacters.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.revolve44.rickandmortycharacters.MainActivity
import com.revolve44.rickandmortycharacters.R
import com.revolve44.rickandmortycharacters.adapter.MainScreenRecycleviewAdapter
import com.revolve44.rickandmortycharacters.utils.Resource
import com.revolve44.rickandmortycharacters.viewmodels.MainScreenViewModel
import timber.log.Timber
import java.lang.Exception

class MainScreenFragment : Fragment(R.layout.fragment_mainscreen), MainScreenRecycleviewAdapter.OnItemClickListener {

    lateinit var mainScreenRecyclerView: RecyclerView



    lateinit var mainScreenRecycleviewAdapter: MainScreenRecycleviewAdapter
    lateinit var mainscreenViewModel: MainScreenViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainScreenRecyclerView = view.findViewById(R.id.main_recycler_view)

        val activity = activity as Context
        mainscreenViewModel =(activity as MainActivity).mainScreenViewModel

        mainScreenRecycleviewAdapter = MainScreenRecycleviewAdapter( this, mainscreenViewModel)
        mainScreenRecyclerView.adapter = mainScreenRecycleviewAdapter
        mainScreenRecyclerView.layoutManager = GridLayoutManager(activity,2)
        mainScreenRecyclerView.setHasFixedSize(false)

        mainscreenViewModel.requestFor15charc.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success ->{
                    response.data?.let { xmsresp ->
                        try {
                            Timber.i("vvv3 "+ mainscreenViewModel.fifteenCharacters.value.toString())

                            mainScreenRecycleviewAdapter.setData(xmsresp.toList())


                        }catch (e: Exception){
                            Timber.e("error when showing recyclerview ${e.message}")
                        }
                    }
                }
                is Resource.Loading -> {
                    Snackbar.make(activity.findViewById(android.R.id.content),"Loading...",Snackbar.LENGTH_LONG)
                }
                is Resource.Error -> {
                    Timber.e("vvv Error load ${response.message}")
                }
            }
        })

    }

    override fun onItemClick(position: Int) {

    }
}