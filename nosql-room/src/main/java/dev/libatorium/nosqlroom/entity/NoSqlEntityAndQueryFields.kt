package dev.libatorium.nosqlroom.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data class used to bind the NoSqlEntity to its Query fields.
 */
internal data class NoSqlEntityAndQueryFields(
    @Embedded val noSqlEntity: NoSqlEntity,
    @Relation(
        parentColumn = NoSqlEntity.COLUMN_ID,
        entityColumn = NoSqlQueryField.COLUMN_ENTITY_ID
    )
    val noSqlQueryFields: List<NoSqlQueryField>?
) {

    /**
     * Apply the queryable fields to the NoSqlEntity.
     */
    fun toNoSqlEntity(): NoSqlEntity {
        return noSqlEntity.apply {
            queryableFields = noSqlQueryFields?.toSet()
        }
    }
}
