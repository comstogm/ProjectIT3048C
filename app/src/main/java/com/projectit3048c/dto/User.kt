package com.projectit3048c.dto

/**
 * A data class for users
 *
 * @property uid ID of the user
 * @property displayName Display name of the user
 */
data class User(val uid: String = "", var displayName: String?) {
}