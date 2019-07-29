package com.kusu.linkedinlogin.model.login

import java.io.Serializable

data class ProfileResponse(
    var id: String,
    var firstName: FirstName,
    var lastName: LastName,
    var profilePicture: ProfilePicture?
) : Serializable