package dev.libatorium.nosqlroom.client

import android.content.Context
import androidx.annotation.RestrictTo
import androidx.room.Room
import com.google.gson.GsonBuilder
import dev.libatorium.nosqlroom.DbModel
import dev.libatorium.nosqlroom.converter.DateConverter
import dev.libatorium.nosqlroom.db.RoomDatabaseClient
import dev.libatorium.nosqlroom.toNoSqlEntity
import dev.libatorium.nosqlroom.util.DateTimeUtils
import dev.libatorium.nosqlroom.util.GsonUtcDateSerializer
import java.util.*

@RestrictTo(RestrictTo.Scope.LIBRARY)
class DatabaseClientImpl(
    context: Context
) : DatabaseClient {

    private val GSON = GsonBuilder()
        // Notice that the default 'setDateFormat' does not allow serialize into UTC time, so
        // we need to make a custom GSON instance for saving to the database.
        .registerTypeAdapter(
            Date::class.java,
            GsonUtcDateSerializer(DateTimeUtils.ISO8601_STANDARD_DATE_TIME_FORMAT)
        )
        // Notice that we're not serializing nulls.
        .create()

    private val database: RoomDatabaseClient = Room.databaseBuilder(
        context,
        RoomDatabaseClient::class.java,
        "${context.packageName}.db"
    ).apply {
        // TODO: Encrypt using SqlCipher.
    }.addTypeConverter(DateConverter())
        .build()

    override suspend fun <T : DbModel> count(userId: String, typeOf: Class<T>): Int {
        val type = typeOf::class.qualifiedName
            ?: throw IllegalArgumentException("Qualified name does not exist for this class.")
        return database.noSqlEntityDao().getCountOfType(userId = userId, typeOf = type) ?: 0
    }

    override suspend fun <T : DbModel> getAll(userId: String, typeOf: Class<T>): List<T> {
        val type = typeOf::class.qualifiedName
            ?: throw IllegalArgumentException("Qualified name does not exist for this class.")

        return database.noSqlEntityDao().getAllEntitiesOfType(userId = userId, typeOf = type)
            ?.map { eAF -> eAF.toNoSqlEntity() }
            ?.map { e -> GSON.fromJson(e.data, typeOf) }
            ?.toList()
            ?: emptyList()
    }

    override suspend fun <T : DbModel> get(userId: String, itemId: String, typeOf: Class<T>): T? {
        val type = typeOf::class.qualifiedName
            ?: throw IllegalArgumentException("Qualified name does not exist for this class.")

        return database.noSqlEntityDao().getEntity(userId = userId, typeOf = type, id = itemId)
            ?.toNoSqlEntity()
            ?.let { e -> GSON.fromJson(e.data, typeOf) }
    }

    override suspend fun save(userId: String, vararg items: DbModel) {
        val entities = items.map { i -> i.toNoSqlEntity(GSON, userId) }.toTypedArray()
        database.noSqlEntityDao().saveEntities(*entities)
    }

    override suspend fun delete(userId: String, vararg itemIds: String) {
        database.noSqlEntityDao().deleteEntities(userId = userId, ids = itemIds)
    }

    override suspend fun <T : DbModel> delete(userId: String, vararg items: T) {
        val entities = items.map { it.toNoSqlEntity(gson = GSON, userId = userId) }.toTypedArray()
        database.noSqlEntityDao().deleteEntities(userId = userId, noSqlEntities = entities)
    }
}