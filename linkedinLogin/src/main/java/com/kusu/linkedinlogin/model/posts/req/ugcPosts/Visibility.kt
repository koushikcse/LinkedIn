package com.kusu.linkedinlogin.model.posts.req.ugcPosts

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Visibility(
    @Expose
    @SerializedName("com.linkedin.ugc.MemberNetworkVisibility")
    private var visibility: String // "PUBLIC" or "CONNECTIONS"
) : Serializable