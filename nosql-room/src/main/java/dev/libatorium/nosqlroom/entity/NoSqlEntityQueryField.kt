package dev.libatorium.nosqlroom.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "queryable_fields", primaryKeys = ["_id", "entity_id"])
internal data class NoSqlEntityQueryField(
    @PrimaryKey @ColumnInfo(name = "_id") val id: String,
    @ColumnInfo(name = "entity_id") val entityId: String,
    val key: String,
    val value: String
)
