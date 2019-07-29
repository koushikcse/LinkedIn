package com.kusu.linkedinlogin

import android.content.Context
import android.content.Intent
import com.kusu.linkedinlogin.helper.Constants
import com.kusu.linkedinlogin.model.SocialUser
import com.kusu.linkedinlogin.network.PostRestService
import com.kusu.linkedinlogin.ui.LinkedinSignInActivity
import java.io.File

class Linkedin {

    companion object {
        var linkedinLoginListener: LinkedinLoginListener? = null

        fun initialize(
            context: Context?,
            clientId: String,
            clientSecret: String,
            redirectUri: String,
            state: String,
            scopes: List<String>
        ) {
            val editor = context?.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)?.edit()
            editor?.putString(Constants.CLIENT_ID, clientId)
            editor?.putString(Constants.CLIENT_SECRET, clientSecret)
            editor?.putString(Constants.REDIRECT_URI, redirectUri)
            editor?.putString(Constants.STATE, state)
            editor?.putStringSet(Constants.SCOPE, scopes.toMutableSet())
            editor?.apply()
        }

        fun login(context: Context?, listener: LinkedinLoginListener) {
            linkedinLoginListener = listener
            val loginActivity = Intent(context, LinkedinSignInActivity::class.java)
            context?.startActivity(loginActivity)
        }

        fun postMessage(
            message: String,
            isPublicVisible: Boolean,
            authToken: String,
            userid: String,
            listener: LinkedinPostResponseListner
        ) {
            val postRestService = PostRestService(authToken, listener)
            postRestService.postMessage(userid, isPublicVisible, message)
        }

        fun postArticle(
            message: String,
            articleUrl: String,
            isPublicVisible: Boolean,
            authToken: String,
            userid: String,
            listener: LinkedinPostResponseListner
        ) {
            val postRestService = PostRestService(authToken, listener)
            postRestService.postArticle(userid, isPublicVisible, message, articleUrl)
        }

        fun postImage(
            message: String,
            imgfile: File,
            isPublicVisible: Boolean,
            authToken: String,
            userid: String,
            listener: LinkedinPostResponseListner
        ) {
            val postRestService = PostRestService(authToken, listener)
            postRestService.postImage(userid, isPublicVisible, message, imgfile)
        }
    }
}

interface LinkedinLoginListener {
    fun successLinkedInLogin(socialUser: SocialUser)

    fun failedLinkedinLogin(error: String)
}

interface LinkedinPostResponseListner {
    fun linkedinPostSuccess()
    fun linkedinPostFailed(error: String)
}