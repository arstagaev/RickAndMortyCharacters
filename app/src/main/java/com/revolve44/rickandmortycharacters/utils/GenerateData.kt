package com.revolve44.rickandmortycharacters.utils

import com.revolve44.rickandmortycharacters.models.Character
import timber.log.Timber


fun getUpdatedList(listOfCharacters: MutableList<Character>, newIdOfCharacter: Int):  MutableList<Character> {

    var arrayPool : MutableList<Character> = mutableListOf()

    // check elements whose marked as elements from pool
    for (i in 0..listOfCharacters.size-1){

        // find element in pool
        if (listOfCharacters.get(i).in_pool){
            //arrayPool.add(listOfCharacters.get(i))

            listOfCharacters.set(i,
                Character(
                listOfCharacters.get(i).id,
                listOfCharacters.get(i).name,
                listOfCharacters.get(i).img_path,
                false))

            return  listOfCharacters
        }

    }

    // if pool is empty
    var randomCharacter = (0..listOfCharacters.size-1).random()

    listOfCharacters.add(Character(
        newIdOfCharacter,
        listOfCharacters.get(randomCharacter).name,
        listOfCharacters.get(randomCharacter).img_path,
        false
    ))
    Timber.i("vvv7 ${listOfCharacters.size}")

    return  listOfCharacters
}