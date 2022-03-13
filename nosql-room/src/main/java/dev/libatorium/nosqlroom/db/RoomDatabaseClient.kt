package dev.libatorium.nosqlroom.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.libatorium.nosqlroom.dao.NoSqlEntityDao
import dev.libatorium.nosqlroom.dao.NoSqlQueryFieldDao
import dev.libatorium.nosqlroom.entity.NoSqlEntity
import dev.libatorium.nosqlroom.entity.NoSqlQueryField

@Database(
    entities = [NoSqlEntity::class, NoSqlQueryField::class],
    version = 1
)
internal abstract class RoomDatabaseClient : RoomDatabase() {
    abstract fun noSqlEntityDao(): NoSqlEntityDao
    abstract fun noSqlEntityQueryFieldDao(): NoSqlQueryFieldDao
}