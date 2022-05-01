package dev.libatorium.nosqlroom.data.provider

import android.content.SharedPreferences
import dev.libatorium.nosqlroom.domain.provider.UserIdProvider

class UserIdProviderImpl(
    private val sharedPreferences: SharedPreferences
) : UserIdProvider {

    companion object {
        private const val KEY_USER_ID = "user_id"
    }

    override val userId: String
        get() = sharedPreferences.getString(KEY_USER_ID, "")!!
}