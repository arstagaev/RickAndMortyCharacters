package com.revolve44.rickandmortycharacters.repository

import android.app.Application
import com.revolve44.rickandmortycharacters.api.RetrofitInstance
import okhttp3.internal.http.RetryAndFollowUpInterceptor

class RickAndMortyRepository(app: Application) {

    suspend fun getFirst15Characters() =RetrofitInstance.apiAlpha.getFirst15Characters()

    suspend fun getCharacter(idOfCharacter : Int) =RetrofitInstance.apiAlpha.getOneCharacter(idOfCharacter)
}