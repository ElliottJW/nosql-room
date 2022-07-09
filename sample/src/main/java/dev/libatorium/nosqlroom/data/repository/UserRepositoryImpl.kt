package dev.libatorium.nosqlroom.data.repository

import android.util.Log
import dev.libatorium.nosqlroom.client.DatabaseClient
import dev.libatorium.nosqlroom.domain.model.User
import dev.libatorium.nosqlroom.domain.provider.UserIdProvider
import dev.libatorium.nosqlroom.domain.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class UserRepositoryImpl(
    private val databaseClient: DatabaseClient,
    private val userIdProvider: UserIdProvider,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    private val userId: String
        get() = userIdProvider.userId

    override fun getAllUsers(): Flow<List<User>> = databaseClient.getAll(userId = userId, User::class).onEach {
        Log.d(this::class.simpleName, "Refresh list.")
    }

    override suspend fun addUser(user: User) {
        withContext(defaultDispatcher) {
            databaseClient.save(userId = userId, user)
        }
    }

    override suspend fun removeUser(itemId: String) {
        withContext(defaultDispatcher) {
            databaseClient.delete(userId = userId, itemId)
        }
    }
}