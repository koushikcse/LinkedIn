package com.kusu.sample

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.kusu.linkedinlogin.Linkedin
import com.kusu.linkedinlogin.LinkedinLoginListener
import com.kusu.linkedinlogin.model.SocialUser
import com.kusu.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        Linkedin.initialize(
            context = applicationContext,
            clientId = "CLIENT_ID", //Client Id of your linkedin app like-> "47sf33fjflf"
            clientSecret = "CLIENT_SECRET", //Client Secret of your linkedin app like-> "Udhfksfeu324uh4"
            redirectUri = "REDIRECT_URL", //Redirect url which has to be add in your linkedin app like-> "https://example.com/auth/linkedin/callback"
            state = "RANDOM_STRING", //For security purpose used to prevent CSRF like-> "nfw4wfhw44r34fkwh"
            scopes = listOf("PERMISSION_OPTIONS") // app permission options like-> "r_liteprofile", "r_emailaddress", "w_member_social"
        )

        binding.btnlogin.setOnClickListener {
            Linkedin.login(this, object : LinkedinLoginListener {
                override fun failedLinkedinLogin(error: String) {
                    Log.e("Linkedin Login faild=> ", error)
                }

                override fun successLinkedInLogin(socialUser: SocialUser) {
                    Log.e("Linkedin user--=> ", socialUser.socialId + ", " + socialUser.email)
                    startActivity(
                        Intent(this@MainActivity, MessageActivity::class.java)
                            .putExtra("Social_data", socialUser)
                    )
                    finish()
                }
            })
        }

    }
}
