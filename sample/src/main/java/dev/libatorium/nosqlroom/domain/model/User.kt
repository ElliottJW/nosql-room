package dev.libatorium.nosqlroom.domain.model

import dev.libatorium.nosqlroom.DbModel

data class User(override val id: String) : DbModel
