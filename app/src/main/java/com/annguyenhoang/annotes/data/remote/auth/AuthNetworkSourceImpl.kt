package com.annguyenhoang.annotes.data.remote.auth

import com.annguyenhoang.annotes.login.LoginUiState
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
        const val USER_NODE_NAME = "users"
    }

    private val users by lazy {
        mutableListOf<UserDto>()
    }

    override fun login(username: String) = callbackFlow {
        databaseRef.child(USER_NODE_NAME).get().addOnSuccessListener { result ->
            val valueNodes = result.value

            // Register
            if (valueNodes == null) {
                val newUser = UserDto(UUID.randomUUID().toString(), username)
                users.add(UserDto(UUID.randomUUID().toString(), Gson().toJson(newUser)))
                val mapOfUser = mutableMapOf<String, String>(USER_NODE_NAME to Gson().toJson(users))
                databaseRef.setValue(mapOfUser)
                trySend(LoginUiState(false, newUser))
                return@addOnSuccessListener
            }

            val usersJSON = valueNodes as String
            val firebaseUsers = Gson().fromJson(usersJSON, Array<UserDto>::class.java).asList()
            users.clear()
            users.addAll(firebaseUsers)
            users.forEach { user ->
                if (user.username == username) {
                    trySend(LoginUiState(false, user))
                    return@addOnSuccessListener
                }
            }
            trySend(LoginUiState(false))
        }.addOnFailureListener {
            Timber.e(it)
            trySend(LoginUiState(false, null, "Error: $it"))
        }

        awaitClose {
            channel.close()
        }
    }

}