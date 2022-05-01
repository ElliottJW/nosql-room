package dev.libatorium.nosqlroom

import android.content.Context
import dev.libatorium.nosqlroom.client.DatabaseClient
import dev.libatorium.nosqlroom.client.RoomDatabaseClientImpl

object NoSqlRoomClient {
    fun getDatabase(context: Context): DatabaseClient {
        return RoomDatabaseClientImpl(context = context)
    }
}