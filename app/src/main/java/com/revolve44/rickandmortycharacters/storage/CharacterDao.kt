package com.revolve44.rickandmortycharacters.storage

import androidx.lifecycle.LiveData
import androidx.room.*
import com.revolve44.rickandmortycharacters.models.Character


@Dao
interface CharacterDao {
    ///// pool of deleting characters in db /////////////////////////////////
    // but i made a decision create pool in the viewmodel
    // because in my plan show this app to devs of server API, so that they include this app in his list https://rickandmortyapi.com/documentation/#libraries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDeletedCharacterToPool(character: Character)

    @Query("SELECT * FROM characters")
    fun getAllCharactersFromPool(): LiveData<List<Character>>

    @Query("DELETE FROM characters")
    suspend fun deleteAllCharactersFromPool()

    @Delete
    suspend fun deleteCharacterFromPool(character: Character)



    // no use now
    //foundation for creating offline mode
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addCharacter(character: Character)
//
//    @Query("SELECT * FROM characters")
//    fun getAllCharacters(): LiveData<List<Character>>
//
//    @Query("DELETE FROM characters")
//    suspend fun deleteAllCharacters()
//
//    @Delete
//    suspend fun deleteOneCharacter(character: Character)




}