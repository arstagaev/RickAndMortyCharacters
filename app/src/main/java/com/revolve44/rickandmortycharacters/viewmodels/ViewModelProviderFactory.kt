package com.revolve44.rickandmortycharacters.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.revolve44.rickandmortycharacters.repository.RickAndMortyRepository

class ViewModelProviderFactory (
        private val app: Application,
        private val repository: RickAndMortyRepository
        ): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainScreenViewModel(app, repository) as T
    }
}