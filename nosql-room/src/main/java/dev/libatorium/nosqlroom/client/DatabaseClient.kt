package dev.libatorium.nosqlroom.client

import dev.libatorium.nosqlroom.DbModel
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

/**
 * DatabaseClient is used for accessing data saved in the NoSqlRoom DB.
 */
interface DatabaseClient {

    /**
     * Returns the count of a certain type of DbModel.
     *
     * @param userId - identifier for the user ID the DbModel is saved by.
     * @param typeOf - type of DbModel
     * @return count of items
     */
    suspend fun <T : DbModel> count(userId: String, typeOf: KClass<T>): Int

    /**
     * Get all items of a certain DbModel type via observable Flow. Any changes that affect
     * the queried type update the list and are emitted by the Flow.
     *
     * @param userId - identifier for the user ID the DbModel is saved by.
     * @param typeOf - type of DbModel
     * @return Flow of all items of the specified type.
     */
    fun <T : DbModel> getAll(userId: String, typeOf: KClass<T>) : Flow<List<T>>

    /**
     * Get a specific DbModel item by ID via observable Flow. Any changes that affect the query,
     * such as deletion, updating, or saving, will be emitted from the Flow.
     *
     * @param userId - identifier for the user ID the DbModel is saved by.
     * @param itemId - the ID of the item stored.
     * @param typeOf - type of DbModel
     * @return
     */
    fun <T: DbModel> get(userId: String, itemId: String, typeOf: KClass<T>) : Flow<T?>

    /**
     * Save any number of DbModels to the database.
     *
     * @param userId - identifier for the user ID the DbModel is saved by.
     * @param items - variable number of DbModels to be saved.
     */
    suspend fun save(userId: String, vararg items: DbModel)

    /**
     * Delete any number of items by ID.
     *
     * @param userId - identifier for the user ID the DbModel is saved by.
     * @param itemIds - IDs of the items intended for deletion.
     * @return count of items deleted.
     */
    suspend fun delete(userId: String, vararg itemIds: String) : Int

    /**
     * Delete any number of items by the object itself.
     *
     * @param userId - identifier for the user ID the DbModel is saved by.
     * @param items - items intended for deletion.
     * @return count of items deleted.
     */
    suspend fun <T : DbModel> delete(userId: String, vararg items: T) : Int
}