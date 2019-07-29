package com.kusu.linkedinlogin.model.posts.res

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UploadMechanism(
    @Expose
    @SerializedName("com.linkedin.digitalmedia.uploading.MediaUploadHttpRequest")
    val mediaUploadHttpRequest: MediaUploadHttpRequest
) : Serializable