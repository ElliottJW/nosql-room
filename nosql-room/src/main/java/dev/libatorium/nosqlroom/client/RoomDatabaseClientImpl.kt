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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.reflect.KClass

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class RoomDatabaseClientImpl(
    context: Context,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DatabaseClient {

    private val _gson = GsonBuilder()
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

    override suspend fun <T : DbModel> count(userId: String, typeOf: KClass<T>): Int =
        withContext(defaultDispatcher) {
            val type = typeOf.qualifiedName ?: throw IllegalArgumentException("Invalid type!")
            database.noSqlEntityDao().getCountOfType(
                userId = userId,
                typeOf = type
            )
        }

    override suspend fun <T : DbModel> get(userId: String, typeOf: KClass<T>): List<T> =
        withContext(defaultDispatcher) {
            val type = typeOf.qualifiedName ?: throw IllegalArgumentException("Invalid type!")
            database.noSqlEntityDao()
                .getAllEntitiesOfType(userId = userId, typeOf = type)
                ?.map { eAF -> eAF.toNoSqlEntity() }
                ?.map { e -> _gson.fromJson(e.data, typeOf.java) }
                ?.toList()
                ?: emptyList()
        }

    override suspend fun <T : DbModel> get(userId: String, itemId: String, typeOf: KClass<T>): T? =
        withContext(defaultDispatcher) {
            val type = typeOf.qualifiedName ?: throw IllegalArgumentException("Invalid type!")
            database.noSqlEntityDao()
                .getEntity(userId = userId, typeOf = type, id = itemId)
                ?.toNoSqlEntity()
                ?.let { e -> _gson.fromJson(e.data, typeOf.java) }
        }

    override suspend fun save(userId: String, vararg items: DbModel) =
        withContext(defaultDispatcher) {
            val entities = items.map { i -> i.toNoSqlEntity(_gson, userId) }.toTypedArray()
            database.noSqlEntityDao().saveEntities(*entities)
        }

    override suspend fun delete(userId: String, vararg itemIds: String): Int =
        withContext(defaultDispatcher) {
            database.noSqlEntityDao().deleteEntities(userId = userId, ids = itemIds)
        }

    override suspend fun <T : DbModel> delete(userId: String, vararg items: T): Int =
        withContext(defaultDispatcher) {
            val entities =
                items.map { it.toNoSqlEntity(gson = _gson, userId = userId) }.toTypedArray()
            database.noSqlEntityDao().deleteEntities(userId = userId, noSqlEntities = entities)
        }
}