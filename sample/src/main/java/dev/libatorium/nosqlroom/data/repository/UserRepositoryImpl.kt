package dev.libatorium.nosqlroom.data.repository

import android.util.Log
import dev.libatorium.nosqlroom.client.DatabaseClient
import dev.libatorium.nosqlroom.domain.model.User
import dev.libatorium.nosqlroom.domain.provider.UserIdProvider
import dev.libatorium.nosqlroom.domain.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl(
    private val databaseClient: DatabaseClient,
    private val userIdProvider: UserIdProvider,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    private val userId: String
        get() = userIdProvider.userId

    override fun getAllUsers(): Flow<List<User>> = flow {
        Log.i(UserRepositoryImpl::class.simpleName, "Emitting all users from repository.")
        emit(databaseClient.get(userId = userId, User::class))
    }.flowOn(defaultDispatcher)

    override fun addUser(user: User) {
        // TODO: ew. use flow.
        CoroutineScope(defaultDispatcher).launch {
            databaseClient.save(userId = userId, user)
        }
    }
}