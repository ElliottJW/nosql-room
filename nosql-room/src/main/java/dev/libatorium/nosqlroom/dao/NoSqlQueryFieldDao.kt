package dev.libatorium.nosqlroom.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import dev.libatorium.nosqlroom.entity.NoSqlQueryField

@Dao
internal interface NoSqlQueryFieldDao {

    @Delete
    suspend fun deleteQueryFields(vararg queryFields: NoSqlQueryField)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQueryFields(vararg queryFields: NoSqlQueryField)
}