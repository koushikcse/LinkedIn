package com.kusu.linkedinlogin.model.posts.req.ugcPosts

import java.io.Serializable

data class Media(
    val status: String = "READY"
) : Serializable {
    var title: ShareCommentary? = null
    var description: ShareCommentary? = null
    var media: String? = null
    var originalUrl: String? = null
}