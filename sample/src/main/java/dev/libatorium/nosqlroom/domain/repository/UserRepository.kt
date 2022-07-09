package dev.libatorium.nosqlroom.domain.repository

import dev.libatorium.nosqlroom.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Basic UserRepository for examples.
 *
 * Developed using best practices described in Google's Advanced Kotlin guides for Coroutines:
 * https://developer.android.com/kotlin/coroutines/coroutines-best-practices#coroutines-data-layer
 */
interface UserRepository {
    fun getAllUsers() : Flow<List<User>>
    suspend fun addUser(user: User)
    suspend fun removeUser(itemId: String)
}