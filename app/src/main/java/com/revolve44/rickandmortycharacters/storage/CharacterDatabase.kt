package com.revolve44.rickandmortycharacters.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.revolve44.rickandmortycharacters.models.Character
import com.revolve44.rickandmortycharacters.models.DeletedCharacter


@Database(
    entities = [Character::class,
               DeletedCharacter::class],
    version = 1
)
abstract class CharacterDatabase : RoomDatabase(){
    abstract val characterDao : CharacterDao

    companion object{
        @Volatile
        private var INSTANCE : CharacterDatabase? = null

        fun getInstance(context: Context) : CharacterDatabase {
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CharacterDatabase::class.java,
                    "character_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}