package com.revolve44.rickandmortycharacters.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.CountDownTimer
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.revolve44.rickandmortycharacters.RickAndMortyApp
import com.revolve44.rickandmortycharacters.models.Character
import com.revolve44.rickandmortycharacters.models.RickAndMortyResponse
import com.revolve44.rickandmortycharacters.repository.RickAndMortyRepository
import com.revolve44.rickandmortycharacters.utils.Constants.Companion.SESSION_INTERVAL
import com.revolve44.rickandmortycharacters.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber

class MainScreenViewModel(app: Application, val repo: RickAndMortyRepository): AndroidViewModel(app) {

    var requestFor15charc : MutableLiveData<Resource<RickAndMortyResponse>> = MutableLiveData()
    val fifteenCharacters : MutableLiveData<MutableList<Character>> = MutableLiveData()

    val requestName : MutableLiveData<String> = MutableLiveData()

    //private var allForecastPool : LiveData<List<ForecastPer3hr>> = repo.getAllForecastFrom5days()

    init {
        startRequestTo5days()

        // coz we now already have 15 characters
        var idOfCharacter = 16

        //every 5 sec we get one character
        val timer = object: CountDownTimer(SESSION_INTERVAL, 5000) {
            override fun onTick(millisUntilFinished: Long) {
                Toast.makeText(app.applicationContext,"Tick",Toast.LENGTH_SHORT).show()

                startRequestEvery5secOneCharacter(idOfCharacter)
                idOfCharacter++

            }

            override fun onFinish() {
                Toast.makeText(app.applicationContext,"Finish",Toast.LENGTH_LONG).show()

            }
        }
        timer.start()




    }


    private fun startRequestTo5days() =viewModelScope.launch {
        safe5daysRequest()

    }

    private fun startRequestEvery5secOneCharacter(idOfCharacter : Int) =viewModelScope.launch {
        safeEvery5secOneCharacterRequest(idOfCharacter)

    }


    private suspend fun safeEvery5secOneCharacterRequest(idOfCharacter : Int) {
        requestFor15charc.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){

                Timber.i("start API request")
                val response = repo.getCharacter(idOfCharacter)
                requestFor15charc.postValue(handleEvery5secOneCharacterRequest(response, idOfCharacter))


            }else{
                requestFor15charc.postValue(Resource.Error("NO INTERNET", null))
                Timber.e("NO INTERNET")

            }

        }catch (t: Throwable){
            when(t) {
                is java.io.IOException -> requestFor15charc.postValue(Resource.Error("Network Failure"))
                else -> requestFor15charc.postValue(Resource.Error("Conversion Error"))
            }
            Timber.e("safeBetaRequest() error: " + t)
        }
    }

    private suspend fun safe5daysRequest() {
        // now status -> Loading
        requestFor15charc.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){

                Timber.i("start API request")
                val response = repo.getFirst15Characters()
                requestFor15charc.postValue(handleRequestFor5days(response))

            }else{
                requestFor15charc.postValue(Resource.Error("NO INTERNET", null))
                Timber.e("NO INTERNET")

            }

        }catch (t: Throwable){
            when(t) {
                is java.io.IOException -> requestFor15charc.postValue(Resource.Error("Network Failure"))
                else -> requestFor15charc.postValue(Resource.Error("Conversion Error"))
            }
            Timber.e("safeBetaRequest() error: " + t)
        }

    }
///////////////////////////////////////////////////////////////// Handle ///////////////////////////////

    private fun handleRequestFor5days(response: Response<RickAndMortyResponse>): Resource<RickAndMortyResponse>? {
        // is successful if {@link #code()} is in the range [200..300)
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                try {
                    Timber.i("body =" + resultResponse.get(0).name)
                    Timber.i("xxx =" + resultResponse.get(0).name)
                    Timber.i("xxx")

                    requestName.postValue(resultResponse.get(0).name + " xxx pizdec")
                    //val listx = resultResponse.size
                    var listOfCharacters: MutableList<Character> = mutableListOf()

                    for (i in 0..resultResponse.size){
                        setOneCharacterInList(Character(resultResponse.get(i).id,resultResponse.get(i).name,"213"))
                    }


                    //Timber.i("vvv ${resultResponse.size}")

                    Timber.i("vvv1 " + listOfCharacters.toString() + "size ${listOfCharacters.size}")
                    Timber.i("vvv2 " + fifteenCharacters.value.toString() + "size ${fifteenCharacters.value?.size}")

                }catch (t: Throwable){
                    Timber.e("xxx handleRequest error ${t.message}")

                }
                return Resource.Success(resultResponse)
            }
        }

        Timber.e("xxx not successful ${response.message()}")
        return Resource.Error(response.message())
    }

    private fun handleEvery5secOneCharacterRequest(response: Response<RickAndMortyResponse>, idOfCharacter: Int): Resource<RickAndMortyResponse>? {
        // is successful if {@link #code()} is in the range [200..300)
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                try {

                    var listOfCharacters: MutableList<Character> = mutableListOf()


                    setOneCharacterInList(Character(resultResponse.get(idOfCharacter).id,resultResponse.get(idOfCharacter).name,"213"))



                    //Timber.i("vvv ${resultResponse.size}")

                    Timber.i("vvv1 " + listOfCharacters.toString() + "size ${listOfCharacters.size}")
                    Timber.i("vvv2 " + fifteenCharacters.value.toString() + "size ${fifteenCharacters.value?.size}")




                }catch (t: Throwable){
                    Timber.e("xxx handleRequest error ${t.message}")

                }
                return Resource.Success(resultResponse)
            }
        }

        Timber.e("xxx not successful ${response.message()}")
        return Resource.Error(response.message())
    }

//    fun getFruitList(): LiveData<List<String?>?>? {
//        if (fifteenCharacters == null) {
//            fifteenCharacters = MutableLiveData<Any>()
//            loadFruits()
//        }
//        return fruitList
//    }
    fun getName(): LiveData<String?>? {
        return requestName
    }


    // add new character of list
    fun setOneCharacterInList(character: Character) {
        fifteenCharacters.value?.add(character)


    }

    fun getAllForecastForChart() : LiveData<MutableList<Character>>{

        return fifteenCharacters
    }

//    private fun deleteALL_table_ForecastCell() = viewModelScope.launch {
//        repo.deleteAllElementsFromTable()
//    }
//
//    private fun saveForecastCell
//            (timestamp :Long, humanTime :String, forecastEnergy :Int,windSpeed: Float,windDirection: Int ) = viewModelScope.launch {
//
//        val forecastCell = ForecastPer3hr(timestamp, 1, humanTime, forecastEnergy,windSpeed,windDirection)
//        repo.saveForecastPer5days(forecastCell)
//    }

//    fun getAllForecastForChart() : LiveData<List<ForecastPer3hr>> {
//
//        return allForecastPool
//    }


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