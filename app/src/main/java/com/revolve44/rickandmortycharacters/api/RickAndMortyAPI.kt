package com.revolve44.rickandmortycharacters.api

import com.revolve44.rickandmortycharacters.models.fiftyelementsrequest.RickAndMortyResponse
import com.revolve44.rickandmortycharacters.models.everyfivesecrequest.OneCharacter
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyAPI {

    // https://rickandmortyapi.com/api/character/1,2,3,4
    // https://rickandmortyapi.com/api/character/16

    // for 5 days
    @GET("api/character/1,2,3,4,5,6,7,8,9,10,11,12,13,14,15")
    suspend fun getFirst15Characters() : Response<RickAndMortyResponse>

    @GET("api/character/{id}")
    suspend fun getOneCharacter(@Path("id") id: Int) : Response<OneCharacter>


}