package com.kusu.linkedinlogin.model.posts.req.registerUpload

import java.io.Serializable

data class ServiceRelationships(
    val relationshipType: String = "OWNER",
    val identifier: String = "urn:li:userGeneratedContent"
) : Serializable