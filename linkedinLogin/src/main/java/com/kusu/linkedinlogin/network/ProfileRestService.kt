package com.kusu.linkedinlogin.network

import com.kusu.linkedinlogin.LinkedinLoginListener
import com.kusu.linkedinlogin.model.LinkedinToken
import com.kusu.linkedinlogin.model.SocialUser
import com.kusu.linkedinlogin.model.email.EmailResponse
import com.kusu.linkedinlogin.model.login.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRestService(
    val linkedinToken: LinkedinToken,
    private val linkedinLoginListener: LinkedinLoginListener
) {

    private val apiInterface: Api = ApiClient.getApiClient(linkedinToken.accessToken).create(Api::class.java)

    fun getLinkedInProfile() {

        val call = apiInterface.getProfile()

        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                val socialUser = SocialUser(
                    response.body()?.id,
                    response.body()?.firstName?.localized?.en_US,
                    response.body()?.lastName?.localized?.en_US
                )
                socialUser.linkedinToken = linkedinToken
                val len = response.body()?.profilePicture?.displayImage?.elements?.size
                if (len != null) {
                    socialUser.profilePicture =
                        response.body()?.profilePicture?.displayImage?.elements?.get(len - 1)?.identifiers?.get(0)
                            ?.identifier
                }
                getLinkedInEmail(socialUser)
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                linkedinLoginListener.failedLinkedinLogin(t.message.toString())
            }
        })
    }

    fun getLinkedInEmail(socialUser: SocialUser) {
        val call = apiInterface.getEmail()

        call.enqueue(object : Callback<EmailResponse> {
            override fun onResponse(call: Call<EmailResponse>, response: Response<EmailResponse>) {
                socialUser.email = response.body()?.elements?.get(0)?.handle?.emailAddress
                linkedinLoginListener.successLinkedInLogin(socialUser)
            }

            override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                linkedinLoginListener.failedLinkedinLogin(t.message.toString())
            }
        })
    }


}
