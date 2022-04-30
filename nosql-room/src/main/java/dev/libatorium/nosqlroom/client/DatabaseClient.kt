package dev.libatorium.nosqlroom.client

import dev.libatorium.nosqlroom.DbModel
import kotlin.reflect.KClass

interface DatabaseClient {
    suspend fun <T : DbModel> count(userId: String, typeOf: KClass<T>): Int

    suspend fun <T : DbModel> get(userId: String, typeOf: KClass<T>) : List<T>

    suspend fun <T: DbModel> get(userId: String, itemId: String, typeOf: KClass<T>) : T?

    suspend fun save(userId: String, vararg items: DbModel)

    suspend fun delete(userId: String, vararg itemIds: String) : Int

    suspend fun <T : DbModel> delete(userId: String, vararg items: T) : Int
}