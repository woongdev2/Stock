package com.teamox.woongstock.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.view.clicks
import com.teamox.woongstock.R
import com.teamox.woongstock.databinding.ActivityCameraBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

class CameraActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1

    private val disposables = CompositeDisposable()
    lateinit var binding: ActivityCameraBinding
    lateinit var mContext: Context
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera)
        mContext = this

        init()
    }

    fun init() {
        val btnCamera = binding.btnCamera.clicks().map { binding.btnCamera }

        val disposable = Observable.mergeArray(btnCamera)
            .throttleFirst(500, TimeUnit.MICROSECONDS, AndroidSchedulers.mainThread())
            .subscribe {
                when (it) {
                    binding.btnCamera -> {
                        dispatchTakePictureIntent()
//                        getImage()
                    }
                }
            }

        disposables.add(disposable)
    }

    fun getImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        
        activityResult.launch(intent)
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()){
//        val uri = it.data!!.data
//
//        Glide.with(this)
//            .load(uri)
//            .into(binding.ivCamera)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.teamox.woongstock.common.CWProvider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT

                    val chooserIntent = Intent.createChooser(intent, "골라주세요 제바알~~")
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePictureIntent))
                    startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.e("test@@@@","::::${currentPhotoPath}")

            Glide.with(this)
                .load("/storage/emulated/0/Android/data/com.teamox.woongstock/files/Pictures/JPEG_20231122_225645_6443947131617432815.jpg")
                .into(binding.ivCamera)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


}


