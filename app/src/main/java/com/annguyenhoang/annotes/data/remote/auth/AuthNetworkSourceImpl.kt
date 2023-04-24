package com.annguyenhoang.annotes.data.remote.auth

import com.annguyenhoang.annotes.presentation.login.LoginUiState
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class AuthNetworkSourceImpl @Inject constructor(
    private val databaseRef: DatabaseReference
) : AuthNetworkSource {

    companion object {
        private const val USER_NODE_NAME = "users"
    }

    override fun login(username: String) = callbackFlow {
        databaseRef.child(USER_NODE_NAME).get().addOnSuccessListener { result ->
            if (!result.exists()) {
                try {
                    val newUser = createAndSendNewUserToFirebase(username, false)
                    trySend(LoginUiState.Data(newUser))
                } catch (e: Exception) {
                    trySend(LoginUiState.Error("Cannot login: $e"))
                }
                return@addOnSuccessListener
            }

            val mapUsers = result.children.associateBy(
                { it.key!! },
                { it.getValue(UserDto::class.java)!! }
            )

            mapUsers.values.forEach { user ->
                if (user.username == username) {
                    trySend(LoginUiState.Data(user))
                    return@addOnSuccessListener
                }
            }

            // set new user
            try {
                val newUser = createAndSendNewUserToFirebase(username, true)
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

    private fun createAndSendNewUserToFirebase(username: String, isParentExist: Boolean): UserDto {
        val userId = UUID.randomUUID().toString()
        val newUser = UserDto(username)
        if (isParentExist) {
            databaseRef.child(USER_NODE_NAME).updateChildren(mapOf(userId to newUser))
        } else {
            databaseRef.child(USER_NODE_NAME).setValue(mutableMapOf(userId to newUser))
        }
        return newUser
    }

}