package com.projectit3048c.dto

import java.util.*

data class Photo(var localUri: String = "",
                 var remoteUri: String = "",
                 var description: String = "",
                 var dateTaken: Date = Date(),
                 var id: String = "") {
}