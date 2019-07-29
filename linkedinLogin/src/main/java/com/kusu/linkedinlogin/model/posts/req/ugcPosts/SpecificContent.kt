package com.kusu.linkedinlogin.model.posts.req.ugcPosts

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SpecificContent(
    @Expose
    @SerializedName("com.linkedin.ugc.ShareContent")
    var shareContent: ShareContent?
):Serializable