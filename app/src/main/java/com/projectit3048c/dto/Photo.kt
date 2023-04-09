package com.projectit3048c.dto

import android.net.Uri
import java.util.*

/**
 * A data class for photos and associated metadata
 *
 * @property localUri The URI for a local copy of the photo
 * @property remoteUri The URI for a remote copy of the photo
 * @property description Description of the photo
 * @property dateTaken Date the photo was taken
 * @property id ID of the photo
 */
data class Photo(var localUri: String = "",
                 var remoteUri: String = "",
                 var description: String = "",
                 var dateTaken: Date = Date(),
                 var id: String = "") {
    init {
        if (!isValidUri(localUri)) {
            throw IllegalArgumentException("Invalid local URI: $localUri")
        }
        if (!isValidUri(remoteUri)) {
            throw IllegalArgumentException("Invalid remote URI: $remoteUri")
        }
    }

    private fun isValidUri(uriString: String): Boolean {
        return try {
            Uri.parse(uriString)
            true
        } catch (e: Exception) {
            false
        }
    }
}