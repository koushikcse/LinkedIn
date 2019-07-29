package com.kusu.linkedinlogin.model.posts.req.ugcPosts

import java.io.Serializable

data class PostRequest(private val author: String) : Serializable {
    val lifecycleState: String = "PUBLISHED"
    var visibility: Visibility =
        Visibility("PUBLIC") // "PUBLIC" or "CONNECTIONS"
    var specificContent: SpecificContent? = null
}