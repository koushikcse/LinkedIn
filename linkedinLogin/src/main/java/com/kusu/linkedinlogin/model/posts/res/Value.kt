package com.kusu.linkedinlogin.model.posts.res

import java.io.Serializable

data class Value(
    val uploadMechanism: UploadMechanism,
    val mediaArtifact: String,
    val asset: String
) : Serializable