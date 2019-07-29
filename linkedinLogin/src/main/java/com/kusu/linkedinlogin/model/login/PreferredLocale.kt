package com.kusu.linkedinlogin.model.login

import java.io.Serializable

data class PreferredLocale(
    var country: String?,
    var language: String?
) : Serializable