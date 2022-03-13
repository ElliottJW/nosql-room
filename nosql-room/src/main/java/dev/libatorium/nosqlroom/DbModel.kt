package dev.libatorium.nosqlroom

import com.google.gson.Gson
import dev.libatorium.nosqlroom.entity.NoSqlEntity
import dev.libatorium.nosqlroom.entity.NoSqlQueryField
import dev.libatorium.nosqlroom.query.DbModelQueryable
import java.io.Serializable
import java.util.*

/**
 * DbMoel represents an object that can be saved to the no-sql database.
 */
interface DbModel : Serializable {

    /**
     * Global identifier for this DbModel.
     */
    val id: String

    /**
     * The date that this model was created. This property does not need to be implemented. It
     * defaults to null if not applicable.
     */
    val createdAt: Date?
        get() = null

    /**
     * The date that this model was last updated. This property does not need to be implemented. It
     * defaults to null if not applicable.
     */
    val updatedAt: Date?
        get() = null
}

internal fun <T : DbModel> T.toNoSqlEntity(gson: Gson, userId: String): NoSqlEntity {
    val type = this::class.qualifiedName
        ?: throw ClassNotFoundException("Unknown class type. Cannot save this to the database.")
    val serializedObject = gson.toJson(this, this::class.java)
    val queryableFields  = (this as? DbModelQueryable)?.queryableFields
        ?.map { field ->
            NoSqlQueryField(
                id = UUID.randomUUID().toString(),
                key = field.key,
                value = field.value,
                entityId = id
            )
        }
        ?.toSet()

    return NoSqlEntity(
        id = id,
        userId = userId,
        type = type,
        data = serializedObject,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        queryableFields = queryableFields
    )
}