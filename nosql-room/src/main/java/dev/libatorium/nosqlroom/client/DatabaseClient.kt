package dev.libatorium.nosqlroom.client

import dev.libatorium.nosqlroom.DbModel

interface DatabaseClient {
    suspend fun <T : DbModel> count(userId: String, typeOf: Class<T>): Int

    suspend fun <T : DbModel> getAll(userId: String, typeOf: Class<T>) : List<T>

    suspend fun <T: DbModel> get(userId: String, itemId: String, typeOf: Class<T>) : T?

    suspend fun save(userId: String, vararg items: DbModel)

    suspend fun delete(userId: String, vararg itemIds: String)

    suspend fun <T : DbModel> delete(userId: String, vararg items: T)
}