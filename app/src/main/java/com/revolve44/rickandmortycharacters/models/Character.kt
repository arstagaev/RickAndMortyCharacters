package com.revolve44.rickandmortycharacters.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
        tableName = "characters"
)
data class Character (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val img_path: String,
    val in_pool: Boolean
    )