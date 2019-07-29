package com.kusu.sample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.kusu.linkedinlogin.Linkedin
import com.kusu.linkedinlogin.LinkedinPostResponseListner
import com.kusu.linkedinlogin.model.SocialUser
import com.kusu.sample.databinding.ActivityMessageBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    private lateinit var social: SocialUser
    private lateinit var img: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message)

        social = intent.getSerializableExtra("Social_data") as SocialUser
        social.let {
            binding.tvName.text = social.firstName + " " + social.lastName
            binding.tvEmail.text = social.email
            Glide.with(this)
                .load(social.profilePicture)
                .into(binding.ivProfile)
        }

        binding.btnPostMessage.setOnClickListener {
            social.linkedinToken?.accessToken?.let { it1 ->
                social.socialId?.let { it2 ->
                    binding.progress.visibility = View.VISIBLE
                    Linkedin.postMessage(
                        binding.tvMessage.text.toString(),
                        true,
                        it1,
                        it2,
                        object : LinkedinPostResponseListner {
                            override fun linkedinPostSuccess() {
                                binding.progress.visibility = View.GONE
                                showText("Message posted..")
                                binding.tvMessage.text.clear()
                            }

                            override fun linkedinPostFailed(error: String) {
                                binding.progress.visibility = View.GONE
                                showText(error)
                            }
                        })
                }
            }
        }

        binding.btnPostArticle.setOnClickListener {
            social.let {
                social.linkedinToken?.accessToken?.let { it1 ->
                    social.socialId?.let { it2 ->
                        binding.progress.visibility = View.VISIBLE
                        Linkedin.postArticle(binding.tvMessage.text.toString(),
                            "https://blog.linkedin.com/",
                            true,
                            it1,
                            it2, object : LinkedinPostResponseListner {
                                override fun linkedinPostSuccess() {
                                    binding.progress.visibility = View.GONE
                                    showText("Message posted..")
                                    binding.tvMessage.text.clear()
                                }

                                override fun linkedinPostFailed(error: String) {
                                    binding.progress.visibility = View.GONE
                                    showText(error)
                                }
                            })
                    }
                }
            }
        }

        binding.btnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                reqPermission()
            } else {
                openCamera()
            }
        }

        binding.btnSelectImg.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                reqPermission()
            } else {
                choosePhotoFromGallary()
            }
        }

        binding.btnPostImage.setOnClickListener {
            social.linkedinToken?.accessToken?.let { it1 ->
                social.socialId?.let { it2 ->
                    binding.progress.visibility = View.VISIBLE
                    Linkedin.postImage(binding.tvMessage.text.toString(), img, true,
                        it1, it2, object : LinkedinPostResponseListner {
                            override fun linkedinPostSuccess() {
                                binding.progress.visibility = View.GONE
                                showText("Message posted..")
                                binding.tvMessage.text.clear()
                            }

                            override fun linkedinPostFailed(error: String) {
                                binding.progress.visibility = View.GONE
                                showText(error)
                            }
                        }
                    )
                }
            }
        }

    }

    private fun reqPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            MY_PERMISSIONS_REQUEST_CAMERA
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            )

            // permission was granted,

                openCamera()

        } else {

            // permission denied, boo! Disable the
            // functionality that depends on this permission.

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.CAMERA)
            ) {

//                showAlert();

            } else {

            }
        }
        return
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQ_CODE && resultCode == Activity.RESULT_OK) {
            val photo = data?.extras?.get("data") as Bitmap
            binding.imageView.setImageBitmap(photo)

            //create a file to write bitmap data
            val f = File(cacheDir, "image.png")
            f.createNewFile()

            val bitmap = photo
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
            val bitmapdata = bos.toByteArray()

            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()

            img = f

        } else if (requestCode == GALLERY_REQ_CODE) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    img = saveImage(bitmap)
                    binding.imageView.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveImage(myBitmap: Bitmap): File {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)

        try {
            val f = File(
                cacheDir, Calendar.getInstance().timeInMillis.toString() + ".jpg"
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                this,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::-" + f.absolutePath)

            return f
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return File("")
    }


    private fun openCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivityForResult(intent, CAMERA_REQ_CODE)
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY_REQ_CODE)
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_CAMERA = 100
        private const val CAMERA_REQ_CODE = 200
        private const val GALLERY_REQ_CODE = 300
    }
}
