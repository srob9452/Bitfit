package com.codepath.bitfit_part1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "calories") val calories: Long?
)