package com.revolve44.rickandmortycharacters.api

import com.revolve44.rickandmortycharacters.models.RickAndMortyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyAPI {

    // http://api.openweathermap.org/data/2.5/forecast?lat=55.5&lon=37.5&cnt=40&appid=ac79fea59e9d15377b787a610a29b784
    // http://api.openweathermap.org/data/2.5/weather?lat=55.5&lon=37.5&appid=ac79fea59e9d15377b787a610a29b784

    // for 5 days
    @GET("api/character/1,2,3,4,5,6,7,8,9,10,11,12,13,14,15")
    suspend fun getFirst15Characters() : Response<RickAndMortyResponse>

    @GET("api/character/")
    suspend fun getOneCharacter(
        @Path("id") id: Int
    ) : Response<RickAndMortyResponse>


}