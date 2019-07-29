package com.kusu.linkedinlogin.model.email

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EmailHandle(
    @Expose
    @SerializedName("handle~")
    var handle: EmailAddess?
) : Serializable
