package dev.libatorium.nosqlroom.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class NoSqlEntity(
    @PrimaryKey @ColumnInfo(name = "_id") val id: String
)
