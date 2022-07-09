package dev.libatorium.nosqlroom.data

import dev.libatorium.nosqlroom.DbModel

data class MockCar(
    override val id: String,
    val brand: String
) : DbModel
