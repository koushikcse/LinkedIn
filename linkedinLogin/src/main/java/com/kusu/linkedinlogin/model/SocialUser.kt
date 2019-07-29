package com.kusu.linkedinlogin.model

import java.io.Serializable

data class SocialUser(
    var socialId: String?,
    var firstName: String?,
    var lastName: String?
) : Serializable {
    var email: String? = null
    var linkedinToken: LinkedinToken? = null
    var profilePicture: String? = null
}