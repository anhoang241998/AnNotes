package com.annguyenhoang.annotes.data.remote.auth

import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
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

    override fun login(username: String) {
        databaseRef.child(USER_NODE_NAME).get().addOnSuccessListener { result ->
            val valueNodes = result.value

            if (valueNodes == null) {
                users.add(UserDto(UUID.randomUUID().toString(), username))
                val mapOfUser = mutableMapOf<String, String>(
                    USER_NODE_NAME to Gson().toJson(users)
                )
                databaseRef.setValue(mapOfUser)
                //                    trySend(true)
                return@addOnSuccessListener
            }

            val userNode = valueNodes
            val b = 0
            //                userNode.let { usersJSONString ->
            //                    val usersFromFirebase =
            //                        Gson().fromJson(usersJSONString, Array<UserDto>::class.java).asList()
            //                    users.clear()
            //                    users.addAll(usersFromFirebase)
            //                    users.forEach { user ->
            //                        if (user.username == username) {
            //                            val a = username
            //                            val b = 0
            //                        }
            //                    }
            //                }
            //                trySend(true)
        }.addOnFailureListener {
            Timber.e(it)
//                trySend(false)
        }
    }

}