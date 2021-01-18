package com.revolve44.rickandmortycharacters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.revolve44.rickandmortycharacters.repository.RickAndMortyRepository
import com.revolve44.rickandmortycharacters.viewmodels.MainScreenViewModel
import com.revolve44.rickandmortycharacters.viewmodels.ViewModelProviderFactory
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    lateinit var mainScreenViewModel: MainScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = RickAndMortyRepository(application)
        val viewModelProviderFactory = ViewModelProviderFactory(application, repository)
        mainScreenViewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainScreenViewModel::class.java)

    }
}