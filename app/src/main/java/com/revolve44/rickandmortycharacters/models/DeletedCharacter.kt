package com.revolve44.rickandmortycharacters.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "deleted_characters"
)
data class DeletedCharacter (
    @PrimaryKey(autoGenerate = false)
    val id_deleted : Int,
    val name_deleted: String,
    val img_path_deleted: String
        )