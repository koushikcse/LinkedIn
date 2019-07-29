package com.kusu.linkedinlogin.model

import java.io.Serializable

data class LinkedinToken(
    val accessToken: String,
    val expiredTime: Long
) : Serializable