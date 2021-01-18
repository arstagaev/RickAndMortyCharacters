package com.revolve44.rickandmortycharacters.repository

import android.app.Application
import com.revolve44.rickandmortycharacters.api.RetrofitInstance
import com.revolve44.rickandmortycharacters.models.Character
import com.revolve44.rickandmortycharacters.storage.CharacterDatabase

class RickAndMortyRepository(app: Application) {

    val db : CharacterDatabase = CharacterDatabase.getInstance(app)

    //remote
    suspend fun getFirst15Characters() =RetrofitInstance.apiAlpha.getFirst15Characters()

    suspend fun getCharacter(idOfCharacter : Int) =RetrofitInstance.apiAlpha.getOneCharacter(idOfCharacter)

    //db
    suspend fun addDeletedCharactertoPool(character: Character) = db.characterDao.addDeletedCharacterToPool(
        Character(character.id,character.name,character.img_path,character.in_pool))

    suspend fun deleteCharacterFromPool(character: Character) = db.characterDao.deleteCharacterFromPool(
        Character(character.id,character.name,character.img_path,character.in_pool))

    fun getAllCharactersFromPool() = db.characterDao.getAllCharactersFromPool()

}