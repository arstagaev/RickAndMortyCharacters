package com.revolve44.rickandmortycharacters.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.revolve44.rickandmortycharacters.RickAndMortyApp
import com.revolve44.rickandmortycharacters.models.Character
import com.revolve44.rickandmortycharacters.models.RickAndMortyResponse
import com.revolve44.rickandmortycharacters.models.forrequests.OneCharacter
import com.revolve44.rickandmortycharacters.repository.RickAndMortyRepository
import com.revolve44.rickandmortycharacters.utils.Constants.Companion.SESSION_INTERVAL
import com.revolve44.rickandmortycharacters.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber

class MainScreenViewModel(app: Application, val repo: RickAndMortyRepository): AndroidViewModel(app) {

    var requestFor15charc : MutableLiveData<Resource<RickAndMortyResponse>> = MutableLiveData()
    var requestFor1charc : MutableLiveData<Resource<OneCharacter>> = MutableLiveData()
    var charactersListMain = MutableLiveData<MutableList<Character>>()


    var listOfCharacters : MutableList<Character> = mutableListOf()


    var newIdOfCharacter = 16



    init {
        startFirst15CharactersRequest()

        // coz we now already have 15 characters



    }

    private fun every5secAddCharacter(){
        //every 5 sec we get one character
        val timer = object: CountDownTimer(SESSION_INTERVAL, 5000) {
            override fun onTick(millisUntilFinished: Long) {
                //Toast.makeText(app.applicationContext,"Tick",Toast.LENGTH_SHORT).show()

                //startRequestEvery5secOneCharacter(idOfCharacter)
                listOfCharacters.add(Character(
                    newIdOfCharacter,
                    listOfCharacters.get((0..14).random()).name,
                    listOfCharacters.get((0..14).random()).img_path))

                charactersListMain.value = listOfCharacters

                newIdOfCharacter++

            }

            override fun onFinish() {
                //Toast.makeText(app.applicationContext,"Finish",Toast.LENGTH_LONG).show()

            }
        }
        timer.start()
    }


    private fun startFirst15CharactersRequest() =viewModelScope.launch {
        safeFirst15CharactersRequest()

    }

    private fun startRequestEvery5secOneCharacter(idOfCharacter : Int) =viewModelScope.launch {
        safeEvery5secOneCharacterRequest(idOfCharacter)

    }


    private suspend fun safeEvery5secOneCharacterRequest(idOfCharacter : Int) {
        requestFor15charc.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){

                Timber.i("vvv start 5sec reqest")
                val response = repo.getCharacter(idOfCharacter)
                requestFor1charc.postValue(handleEvery5secOneCharacterRequest(response, idOfCharacter))


            }else{
                requestFor15charc.postValue(Resource.Error("NO INTERNET", null))
                Timber.e("NO INTERNET")

            }

        }catch (t: Throwable){
            when(t) {
                is java.io.IOException -> requestFor15charc.postValue(Resource.Error("Network Failure: ${t.message}"))
                else -> requestFor15charc.postValue(Resource.Error("Conversion Error: ${t.message}"))
            }
            Timber.e("safeEvery5secOneCharacterRequest() error: " + t)
        }
    }

    private suspend fun safeFirst15CharactersRequest() {
        // now status -> Loading
        requestFor15charc.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){

                Timber.i("vvv start API request 15")
                val response = repo.getFirst15Characters()
                requestFor15charc.postValue(handleRequest(response))

            }else{
                requestFor15charc.postValue(Resource.Error("NO INTERNET", null))
                Timber.e("NO INTERNET")

            }

        }catch (t: Throwable){
            when(t) {
                is java.io.IOException -> requestFor15charc.postValue(Resource.Error("Network Failure"))
                else -> requestFor15charc.postValue(Resource.Error("Conversion Error"))
            }
            Timber.e("safeFirst15CharactersRequest() error: " + t)
        }

    }
///////////////////////////////////////////////////////////////// Handle ///////////////////////////////

    private fun handleRequest(response: Response<RickAndMortyResponse>): Resource<RickAndMortyResponse>? {
        // is successful if {@link #code()} is in the range [200..300)
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                try {
                    Timber.i("body =" + resultResponse.get(1))


                    //Timber.i("vvv1 " + resultResponse.toString() + "size ${CharactersListMain.value?.size}")
                    for (i in 0..resultResponse.size-1){

                        listOfCharacters.add(Character(resultResponse.get(i).id,resultResponse.get(i).name,resultResponse.get(i).image))
                    }
                    charactersListMain.value = listOfCharacters
                    every5secAddCharacter()

                    //Timber.i("vvv2 " + CharactersListMain.value.toString() + "size ${CharactersListMain.value?.size}")

                }catch (t: Throwable){
                    Timber.e("xxx handleRequest error ${t.message}")

                }
                return Resource.Success(resultResponse)
            }
        }

        Timber.e("xxx not successful ${response.message()}")
        return Resource.Error(response.message())
    }

    private fun handleEvery5secOneCharacterRequest(response: Response<OneCharacter>, idOfCharacter: Int): Resource<OneCharacter>? {
        // is successful if {@link #code()} is in the range [200..300)
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                try {

                    //var listOfCharacters: MutableList<Character> = mutableListOf()
                    listOfCharacters.add(Character(resultResponse.id,resultResponse.name,resultResponse.image))
                    charactersListMain.value = listOfCharacters
                    //setOneCharacterInList(Character(resultResponse.get(0).id,resultResponse.get(0).name,resultResponse.get(0).image))


                    Timber.i("vvv2 " + charactersListMain.value.toString() + "size ${charactersListMain.value?.size}")


                }catch (t: Throwable){
                    Timber.e("xxx handleRequest error ${t.message}")

                }
                return Resource.Success(resultResponse)
            }
        }

        Timber.e("xxx not successful ${response.message()}")
        return Resource.Error(response.message())
    }



    fun getAllCharacters() : LiveData<MutableList<Character>>{

        return charactersListMain
    }



    //check Internet connection
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<RickAndMortyApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        // below is mean - current SDK >= Marsmellow SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                    connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


}