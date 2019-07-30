# Android-LinkedIn-Login-Post-Integration
Android LinkedIn login integration to get user lite profile with email and post image, message and article in LinkedIn profile.

[![](https://jitpack.io/v/koushikcse/LinkedIn.svg)](https://jitpack.io/#koushikcse/LinkedIn)

![LinkedIn Integration demo](https://github.com/koushikcse/LinkedIn/blob/master/linkedindemo.gif)

## Add to Gradle

Add this to your project level `build.gradle` file

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

And then add this to your module level `build.gradle` file

```gradle
dependencies {
    implementation "com.github.koushikcse:LinkedIn:${latest-version}"
}
```
## How it works
In your `application` inside `onCreate` method add following line to initialize linkedIn with required details. If you only want user lite profile and email for linkedin login integratin then provide permission `scope=listOf("r_liteprofile", "r_emailaddress")` or if you want to post message with image/article link then provide permission `scope=listOf("r_liteprofile", "r_emailaddress","w_member_social")`.

###### Kotlin

```
Linkedin.initialize(
            context = applicationContext,
            clientId = "CLIENT_ID", //Client Id of your linkedin app like-> "47sf33fjflf"
            clientSecret = "CLIENT_SECRET", //Client Secret of your linkedin app like-> "Udhfksfeu324uh4"
            redirectUri = "REDIRECT_URL", //Redirect url which has to be add in your linkedin app like-> "https://example.com/auth/linkedin/callback"
            state = "RANDOM_STRING", //For security purpose used to prevent CSRF like-> "nfw4wfhw44r34fkwh"
            scopes = listOf("PERMISSION_OPTIONS") // app permission options like-> "r_liteprofile", "r_emailaddress", "w_member_social"
        )
```
###### Java
```
Linkedin.Companion.initialize(getApplicationContext(),
                "CLIENT_ID",  //Client Id of your linkedin app like-> "47sf33fjflf"
                "CLIENT_SECRET", //Client Secret of your linkedin app like-> "Udhfksfeu324uh4"
                "REDIRECT_URL", //Redirect url which has to be add in your linkedin app like-> "https://example.com/auth/linkedin/callback"
                "RANDOM_STRING", //For security purpose used to prevent CSRF like-> "nfw4wfhw44r34fkwh"
                Arrays.asList("PERMISSION_OPTIONS") // app permission options like-> "r_liteprofile", "r_emailaddress", "w_member_social"
                );
```

### Setup Login Listener
Then in your `activity` on `button` click add these lines. In `LinkedinLoginListener` callback you will get failed and success listner override methods. For success linkedin login you will get user details and token in `SocialUser` object.

###### Kotlin
```
btnlogin.setOnClickListener {
            Linkedin.login(this, object : LinkedinLoginListener {
                override fun failedLinkedinLogin(error: String) {
                    //todo failed functionality
                }

                override fun successLinkedInLogin(socialUser: SocialUser) {
                    // todo success functionality
                }
            })
        }
 ```
 ###### Java
 ```
 btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linkedin.Companion.login(LinkedinActivity.this, new LinkedinLoginListener() {
                    @Override
                    public void successLinkedInLogin(@NotNull SocialUser socialUser) {
                         // todo success functionality
                    }

                    @Override
                    public void failedLinkedinLogin(@NotNull String s) {
                        //todo failed functionality
                    }
                });
            }
        });
```

### Setup Message Post Listener
In `LinkedinLoginListener` for successful login you will get `socialUser` object, from which you will get `accessToken` and `socialId`. Then you can post message in LinkedIn profile with these `accessToken` and `socialId`. There are three methods for posting message in LinkedIn profile. For normal message call `postMessage`, for posting a message with article link call
`postArticle` method and for posting an image with text in LinkedIn profile call `postImage` method. Check the following uses of these methods for better understanding.

##### Post Normal Text Message
###### Kotlin
```
btnPostMessage.setOnClickListener {
	binding.progress.visibility = View.VISIBLE
        Linkedin.postMessage(
        	"MESSAGE", // message you want to post
        	true, // Visibility -> true for public and false for only connections 
                "ACCESS_TOKEN", //access token which one you will get from login response
                "SOCIAL_ID", //social id which one you will get from login response
                 object : LinkedinPostResponseListner {
		 override fun linkedinPostSuccess() {
                 	binding.progress.visibility = View.GONE
                        // todo success functionality
                 }

                 override fun linkedinPostFailed(error: String) {
                 	binding.progress.visibility = View.GONE
                        //todo failed functionality
                 }
            })
      }
```
###### Java
```
btnPostMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                Linkedin.Companion.postMessage("MESSAGE", // message you want to post
                        true, // Visibility -> true for public and false for only connections 
                        "ACCESS_TOKEN", //access token which one you will get from login response
                        "SOCIAL_ID", //social id which one you will get from login response
                        new LinkedinPostResponseListner() {
                            @Override
                            public void linkedinPostSuccess() {
                                hideLoading();
                                // todo success functionality
                            }

                            @Override
                            public void linkedinPostFailed(@NotNull String s) {
                                hideLoading();
			       //todo failed functionality
                            }
                        }
                );
            }
        });
```
##### Post Article With Text Message
###### Kotlin
```
btnPostArticle.setOnClickListener {
	binding.progress.visibility = View.VISIBLE
        Linkedin.postArticle(
        	"MESSAGE", // message you want to post
		"ARTICLE_URL" //url of the article like->"https://blog.linkedin.com/"
        	true, // Visibility -> true for public and false for only connections 
                "ACCESS_TOKEN", //access token which one you will get from login response
                "SOCIAL_ID", //social id which one you will get from login response
                 object : LinkedinPostResponseListner {
		 override fun linkedinPostSuccess() {
                 	binding.progress.visibility = View.GONE
                        // todo success functionality
                 }

                 override fun linkedinPostFailed(error: String) {
                 	binding.progress.visibility = View.GONE
                        //todo failed functionality
                 }
            })
      }
```
###### Java
```
btnPostArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                Linkedin.Companion.postArticle("MESSAGE", // message you want to post
			"ARTICLE_URL" //url of the article like->"https://blog.linkedin.com/"
                        true, // Visibility -> true for public and false for only connections 
                        "ACCESS_TOKEN", //access token which one you will get from login response
                        "SOCIAL_ID", //social id which one you will get from login response
                        new LinkedinPostResponseListner() {
                            @Override
                            public void linkedinPostSuccess() {
                                hideLoading();
                                // todo success functionality
                            }

                            @Override
                            public void linkedinPostFailed(@NotNull String s) {
                                hideLoading();
			       //todo failed functionality
                            }
                        }
                );
            }
        });
```
##### Post Image With Text Message
###### Kotlin
```
btnPostImage.setOnClickListener {
	binding.progress.visibility = View.VISIBLE
        Linkedin.postImage(
        	"MESSAGE", // message you want to post
		"IMAGE_FILE" //Image file you want to post
        	true, // Visibility -> true for public and false for only connections 
                "ACCESS_TOKEN", //access token which one you will get from login response
                "SOCIAL_ID", //social id which one you will get from login response
                 object : LinkedinPostResponseListner {
		 override fun linkedinPostSuccess() {
                 	binding.progress.visibility = View.GONE
                        // todo success functionality
                 }

                 override fun linkedinPostFailed(error: String) {
                 	binding.progress.visibility = View.GONE
                        //todo failed functionality
                 }
            })
      }
```
###### Java
```
btnPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                Linkedin.Companion.postImage("MESSAGE", // message you want to post
			"IMAGE_FILE" //Image file you want to post
                        true, // Visibility -> true for public and false for only connections 
                        "ACCESS_TOKEN", //access token which one you will get from login response
                        "SOCIAL_ID", //social id which one you will get from login response
                        new LinkedinPostResponseListner() {
                            @Override
                            public void linkedinPostSuccess() {
                                hideLoading();
                                // todo success functionality
                            }

                            @Override
                            public void linkedinPostFailed(@NotNull String s) {
                                hideLoading();
			       //todo failed functionality
                            }
                        }
                );
            }
        });
```


# LICENSE

MIT License

Copyright (c) 2019 Koushik Mondal

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
