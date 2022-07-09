package dev.libatorium.nosqlroom.data

import dev.libatorium.nosqlroom.DbModel

data class MockUser(
    override val id: String,
    val firstName: String,
    val lastName: String,
    val emailAddress: String
) : DbModel
