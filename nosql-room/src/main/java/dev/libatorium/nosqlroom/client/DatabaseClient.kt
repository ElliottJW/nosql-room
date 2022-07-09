package dev.libatorium.nosqlroom.client

import dev.libatorium.nosqlroom.DbModel
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

interface DatabaseClient {
    suspend fun <T : DbModel> count(userId: String, typeOf: KClass<T>): Int

    fun <T : DbModel> getAll(userId: String, typeOf: KClass<T>) : Flow<List<T>>

    fun <T: DbModel> get(userId: String, itemId: String, typeOf: KClass<T>) : Flow<T?>

    suspend fun save(userId: String, vararg items: DbModel)

    suspend fun delete(userId: String, vararg itemIds: String)

    suspend fun <T : DbModel> delete(userId: String, vararg items: T)
}