package com.annguyenhoang.annotes.data.remote.auth

import com.annguyenhoang.annotes.presentation.login.LoginUiState
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class AuthNetworkSourceImpl @Inject constructor(
    private val databaseRef: DatabaseReference
) : AuthNetworkSource {

    companion object {
        private const val USER_NODE_NAME = "users"
    }

    private val users by lazy {
        mutableListOf<UserDto>()
    }

    override fun login(username: String) = callbackFlow {
        databaseRef.child(USER_NODE_NAME).get().addOnSuccessListener { result ->
            val valueNodes = result.value

            // first time
            if (valueNodes == null) {
                try {
                    val newUser = createAndSendNewUserToFirebase(username)
                    trySend(LoginUiState.Data(newUser))
                } catch (e: Exception) {
                    trySend(LoginUiState.Error("Cannot login: $e"))
                }
                return@addOnSuccessListener
            }

            val usersJSON = valueNodes as String
            val firebaseUsers = Gson().fromJson(usersJSON, Array<UserDto>::class.java).asList()
            users.clear()
            users.addAll(firebaseUsers)
            users.forEach { user ->
                if (user.username == username) {
                    trySend(LoginUiState.Data(user))
                    return@addOnSuccessListener
                }
            }

            // set new user
            try {
                val newUser = createAndSendNewUserToFirebase(username)
                trySend(LoginUiState.Data(newUser))
            } catch (e: Exception) {
                trySend(LoginUiState.Error("Cannot login: $e"))
            }
        }.addOnFailureListener {
            Timber.e(it)
            trySend(LoginUiState.Error("Error: $it"))
        }

        awaitClose {
            channel.close()
        }
    }

    private fun createAndSendNewUserToFirebase(username: String): UserDto {
        val newUser = UserDto(UUID.randomUUID().toString(), username)
        users.add(UserDto(UUID.randomUUID().toString(), Gson().toJson(newUser)))
        val mapOfUser = mutableMapOf<String, String>(USER_NODE_NAME to Gson().toJson(users))
        databaseRef.setValue(mapOfUser)
        return newUser
    }

}