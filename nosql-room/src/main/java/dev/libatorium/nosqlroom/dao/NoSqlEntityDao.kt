package dev.libatorium.nosqlroom.dao

import androidx.room.*
import dev.libatorium.nosqlroom.DbModel
import dev.libatorium.nosqlroom.db.RoomDatabaseClient
import dev.libatorium.nosqlroom.entity.NoSqlEntity
import dev.libatorium.nosqlroom.entity.NoSqlEntityAndQueryFields
import dev.libatorium.nosqlroom.entity.NoSqlQueryField
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

@Dao
internal abstract class NoSqlEntityDao(
    private val database: RoomDatabaseClient
) {

    private val noSqlQueryFieldDao: NoSqlQueryFieldDao
        get() = database.noSqlEntityQueryFieldDao()

    @Transaction
    @Insert
    suspend fun saveEntities(vararg noSqlEntities: NoSqlEntity) {
        _saveEntities(*noSqlEntities)
        val queryFields = noSqlEntities.mapNotNull { it.queryableFields }.flatten().toTypedArray()
        noSqlQueryFieldDao.saveQueryFields(*queryFields)
    }

    @Transaction
    @Delete
    suspend fun deleteEntities(userId: String, vararg noSqlEntities: NoSqlEntity) {
        // Delete the entities themselves.
        val ids = noSqlEntities.map { e -> e.id }.toTypedArray()
        deleteEntities(userId = userId, ids = ids)

        // Also delete the query fields.
        val queryFields = noSqlEntities.mapNotNull { it.queryableFields }.flatten().toTypedArray()
        noSqlQueryFieldDao.deleteQueryFields(*queryFields)
    }

    @Query("DELETE FROM nosql_entities WHERE user_id = :userId AND _id IN (:ids)")
    abstract suspend fun deleteEntities(userId: String, vararg ids: String)

    @Transaction /* POJO is a NoSqlEntity + NoSqlQueryField */
    @Query("SELECT * FROM nosql_entities WHERE user_id = :userId AND type = :typeOf AND _id = :id")
    abstract fun getEntity(userId: String, typeOf: String, id: String) : Flow<NoSqlEntityAndQueryFields?>

    @Transaction /* POJO is a NoSqlEntity + NoSqlQueryField */
    @Query("SELECT * FROM nosql_entities WHERE user_id = :userId AND type = :typeOf")
    abstract fun getAllEntitiesOfType(userId: String, typeOf: String) : Flow<List<NoSqlEntityAndQueryFields>?>

    @Query("SELECT COUNT(_id) FROM nosql_entities WHERE user_id = :userId AND type = :typeOf")
    abstract suspend fun getCountOfType(userId: String, typeOf: String) : Int

    // PRIVATE ************************************************************************************

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun _saveEntities(vararg noSqlEntities: NoSqlEntity)
}