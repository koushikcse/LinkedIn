package com.kusu.linkedinlogin.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProfilePicture(
    @Expose
    @SerializedName("displayImage~")
    var displayImage: DisplayImage?
) : Serializable