package com.kusu.linkedinlogin.model.posts.req.registerUpload

import java.io.Serializable

data class RegisterUploadRequest(
    val owner: String,
    val recipes: Array<String> = arrayOf("urn:li:digitalmediaRecipe:feedshare-image"),
    val serviceRelationships: List<ServiceRelationships>
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegisterUploadRequest

        if (owner != other.owner) return false
        if (!recipes.contentEquals(other.recipes)) return false
        if (serviceRelationships != other.serviceRelationships) return false

        return true
    }

    override fun hashCode(): Int {
        var result = owner.hashCode()
        result = 31 * result + recipes.contentHashCode()
        result = 31 * result + serviceRelationships.hashCode()
        return result
    }
}