package dev.libatorium.nosqlroom.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = NoSqlQueryField.TABLE_NAME)
internal data class NoSqlQueryField(
    @PrimaryKey @ColumnInfo(name = COLUMN_ID) val id: String,
    @ColumnInfo(name = COLUMN_ENTITY_ID) val entityId: String,
    val key: String,
    val value: String
) {
    companion object {
        const val TABLE_NAME = "queryable_fields"
        const val COLUMN_ID = "_id"
        const val COLUMN_ENTITY_ID = "entity_id"
    }
}
