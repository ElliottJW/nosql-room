package dev.libatorium.nosqlroom.domain.repository

import dev.libatorium.nosqlroom.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers() : Flow<List<User>>
    fun addUser(user: User)
}