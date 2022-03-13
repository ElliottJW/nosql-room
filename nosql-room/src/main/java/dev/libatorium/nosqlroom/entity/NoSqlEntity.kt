package dev.libatorium.nosqlroom.entity

import androidx.room.*
import dev.libatorium.nosqlroom.converter.DateConverter
import java.util.*

@Entity(tableName = NoSqlEntity.TABLE_NAME)
@TypeConverters(DateConverter::class)
internal data class NoSqlEntity @JvmOverloads constructor(
    @PrimaryKey @ColumnInfo(name = COLUMN_ID) val id: String,
    @ColumnInfo(name = "user_id") val userId: String,
    val type: String,
    val data: String,
    @ColumnInfo(name = "created_date") val createdAt: Date?,
    @ColumnInfo(name = "updated_date") val updatedAt: Date?,
    @Ignore var queryableFields: Set<NoSqlQueryField>? = null
) {
    companion object {
        const val TABLE_NAME = "nosql_entities"
        const val COLUMN_ID = "_id"
    }
}
