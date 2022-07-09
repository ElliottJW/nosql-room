package dev.libatorium.nosqlroom.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.libatorium.nosqlroom.domain.model.User
import dev.libatorium.nosqlroom.domain.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SampleAppViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val users = userRepository.getAllUsers().shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1)

    fun onAddUser() {
        viewModelScope.launch {
            val randomUserId = UUID.randomUUID().toString()
            userRepository.addUser(User(id = randomUserId))
        }
    }

    fun onDeleteUser(user: User) {
        viewModelScope.launch {
            userRepository.removeUser(user.id)
        }
    }
}