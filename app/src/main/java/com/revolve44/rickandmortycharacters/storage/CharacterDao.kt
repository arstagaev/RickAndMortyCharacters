package com.revolve44.rickandmortycharacters.storage

import androidx.lifecycle.LiveData
import androidx.room.*
import com.revolve44.rickandmortycharacters.models.Character
import com.revolve44.rickandmortycharacters.models.DeletedCharacter


@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPassword(character: Character)

    @Query("SELECT * FROM characters")
    fun getAllPasswords(): LiveData<List<Character>>

    @Query("DELETE FROM characters")
    suspend fun deleteAllPasswords()

    @Delete
    suspend fun deleteOnePassword(character: Character)

    ///// trashbox /////////////////////////////////
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDeletedPasswordtoTrashbox(deletedCharacter: DeletedCharacter)

    @Query("SELECT * FROM deleted_characters")
    fun getAllPasswordsFromTrashbox(): LiveData<List<DeletedCharacter>>

    @Query("DELETE FROM deleted_characters")
    suspend fun deleteAllPasswordsInTrashbox()

    @Delete
    suspend fun restoreOnePasswordFromTrashbox(deletedCharacter: DeletedCharacter)


}