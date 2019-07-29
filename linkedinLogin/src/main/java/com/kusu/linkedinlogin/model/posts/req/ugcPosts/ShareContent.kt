package com.kusu.linkedinlogin.model.posts.req.ugcPosts

import java.io.Serializable

data class ShareContent(
    var shareCommentary: ShareCommentary,
    var shareMediaCategory: String = "NONE" //"NONE" or "ARTICLE" or "IMAGE"
) : Serializable {
    var media: List<Media>? = null

}