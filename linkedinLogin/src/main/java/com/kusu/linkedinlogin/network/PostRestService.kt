package com.kusu.linkedinlogin.network

import android.net.Uri
import android.os.Build
import com.kusu.linkedinlogin.LinkedinPostResponseListner
import com.kusu.linkedinlogin.helper.Constants
import com.kusu.linkedinlogin.model.posts.req.registerUpload.MainRequest
import com.kusu.linkedinlogin.model.posts.req.registerUpload.RegisterUploadRequest
import com.kusu.linkedinlogin.model.posts.req.registerUpload.ServiceRelationships
import com.kusu.linkedinlogin.model.posts.req.ugcPosts.*
import com.kusu.linkedinlogin.model.posts.res.PostResponse
import com.kusu.linkedinlogin.model.posts.res.RegisterUploadResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files


class PostRestService(
    private val authToken: String,
    private val linkedinPostResponseListner: LinkedinPostResponseListner
) {
    private val apiInterface: Api = ApiClient.getApiClient(authToken).create(Api::class.java)

    fun postMessage(userid: String, publicVisibile: Boolean, msg: String) {
        val postRequest = PostRequest(Constants.URN_PERSON + userid)
        if (publicVisibile)
            postRequest.visibility =
                Visibility(Constants.PUBLICLY_VISIBILE)
        else
            postRequest.visibility =
                Visibility(Constants.CONNECTION_VISIBILE)

        postRequest.specificContent =
            SpecificContent(
                ShareContent(
                    ShareCommentary(msg),
                    Constants.MESSAGE_MEDIA_CATEGORY
                )
            )

        val call = apiInterface.postMessage(postRequest)

        call.enqueue(object : Callback<PostResponse> {
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                linkedinPostResponseListner.linkedinPostFailed(t.message.toString())
            }

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                linkedinPostResponseListner.linkedinPostSuccess()
            }
        })

    }

    fun postArticle(userid: String, publicVisibile: Boolean, msg: String, articleUrl: String) {
        val postRequest = PostRequest(Constants.URN_PERSON + userid)
        if (publicVisibile)
            postRequest.visibility =
                Visibility(Constants.PUBLICLY_VISIBILE)
        else
            postRequest.visibility =
                Visibility(Constants.CONNECTION_VISIBILE)

        val shareContent = ShareContent(
            ShareCommentary(msg), Constants.ARTICLE_MEDIA_CATEGORY
        )

        val media = Media(Constants.READY)
        media.title = ShareCommentary(msg)
        media.description = ShareCommentary(msg)
        media.originalUrl = articleUrl

        shareContent.media = listOf(media)

        postRequest.specificContent = SpecificContent(shareContent)

        val call = apiInterface.postMessage(postRequest)

        call.enqueue(object : Callback<PostResponse> {
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                linkedinPostResponseListner.linkedinPostFailed(t.message.toString())
            }

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                linkedinPostResponseListner.linkedinPostSuccess()
            }
        })

    }

    fun postImage(userid: String, publicVisibile: Boolean, msg: String, img: File) {
        val serviceRelationships = ServiceRelationships()
        val registerUploadRequest = RegisterUploadRequest(
            Constants.URN_PERSON + userid,
            arrayOf(Constants.URN_RECEIPES),
            listOf(serviceRelationships)
        )
        val req = MainRequest(registerUploadRequest)

        val callRegisterUpload = apiInterface.registerUpload(req)
        callRegisterUpload.enqueue(object : Callback<RegisterUploadResponse> {
            override fun onFailure(call: Call<RegisterUploadResponse>, t: Throwable) {
                linkedinPostResponseListner.linkedinPostFailed(t.message.toString())
            }

            override fun onResponse(call: Call<RegisterUploadResponse>, response: Response<RegisterUploadResponse>) {
                if (response.code() == 200) {
                    response.body()?.value?.asset?.let {
                        asset = it
                    }
                    val callUploadImg =
                        response.body()?.value?.uploadMechanism?.mediaUploadHttpRequest?.uploadUrl?.let {
                            val url = it
                            val temp = "https://api.linkedin.com/"

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                apiInterface.uploadMedia(
                                    Uri.parse(url.substring(temp.length)),
                                    RequestBody.create(
                                        MediaType.parse("application/octet"),
                                        Files.readAllBytes(img.toPath())
                                    )
                                )
                            } else {
                                apiInterface.uploadMedia(
                                    Uri.parse(url.substring(temp.length)),
                                    RequestBody.create(
                                        MediaType.parse("application/octet"),
                                        getFileArray(img)
                                    )
                                )

                            }
                        }
                    callUploadImg?.enqueue(object : Callback<Void> {
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            linkedinPostResponseListner.linkedinPostFailed(t.message.toString())
                        }

                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.code() == 201) {
                                val postRequest =
                                    PostRequest(Constants.URN_PERSON + userid)
                                if (publicVisibile)
                                    postRequest.visibility =
                                        Visibility(Constants.PUBLICLY_VISIBILE)
                                else
                                    postRequest.visibility =
                                        Visibility(Constants.CONNECTION_VISIBILE)

                                val shareContent = ShareContent(
                                    ShareCommentary(msg),
                                    Constants.IMAGE_MEDIA_CATEGORY
                                )

                                val media = Media(Constants.READY)
                                media.title = ShareCommentary(msg)
                                media.description = ShareCommentary(msg)
                                media.media = asset

                                shareContent.media = listOf(media)

                                postRequest.specificContent =
                                    SpecificContent(shareContent)

                                val postcall = apiInterface.postMessage(postRequest)

                                postcall.enqueue(object : Callback<PostResponse> {
                                    override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                                        linkedinPostResponseListner.linkedinPostFailed(t.message.toString())
                                    }

                                    override fun onResponse(
                                        call: Call<PostResponse>,
                                        response: Response<PostResponse>
                                    ) {
                                        if (response.code() == 201)
                                            linkedinPostResponseListner.linkedinPostSuccess()
                                        else
                                            linkedinPostResponseListner.linkedinPostFailed(response.message())
                                    }
                                })

                            } else {
                                linkedinPostResponseListner.linkedinPostFailed("File Upload Failed")
                            }
                        }
                    })
                } else {
                    linkedinPostResponseListner.linkedinPostFailed(response.message())
                }

            }
        })


    }

    fun getFileArray(f: File): ByteArray {
        val fileInputStream = FileInputStream(f)

        val byteLength = f.length() // byte count of the file-content

        val filecontent = ByteArray(byteLength.toInt())
        fileInputStream.read(filecontent, 0, byteLength.toInt())
        return filecontent
    }

    companion object {
        var asset: String? = null
    }

}