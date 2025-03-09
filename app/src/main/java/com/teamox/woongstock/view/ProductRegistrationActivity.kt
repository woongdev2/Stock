package com.teamox.woongstock.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.teamox.woongstock.R
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.databinding.ActivityProductRegistrationBinding
import com.teamox.woongstock.viewmodel.ProductRegistrationViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProductRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductRegistrationBinding
    private lateinit var viewModel: ProductRegistrationViewModel
    private lateinit var mContext: Context
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_registration)
        viewModel = ProductRegistrationViewModel(DatabaseRepository(this))

        mContext = this

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        init()
    }

    private fun init(){
        setObserve()
        setImageButton()
        setClickCameraBtn()
    }

    private fun setObserve(){
        viewModel.text.observe( this) {
            if(it.equals("ok")){
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
    private fun setImageButton(){
        Glide.with(this)
            .load(R.drawable.camera)
            .transform(CenterCrop(), RoundedCorners(50) )
            .into(binding.ibCamera)
    }

    private fun setClickCameraBtn(){
        binding.ibCamera.setOnClickListener {
            dispatchTakePictureIntent()
        }
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

                    val chooserIntent = Intent.createChooser(intent, "이미지를 가져올 방법을 선택하세요")
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePictureIntent))
                    startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            data?.data?.let { uri ->
                Glide.with(this)
                    .load(uri)
                    .transform(CenterCrop(), RoundedCorners(50) )
                    .into(binding.ibCamera)
                deleteImageFile(currentPhotoPath)
                currentPhotoPath = saveImageToInternalStorage(uri)!!
                viewModel.upDateImage(currentPhotoPath)
            } ?: run{
                Glide.with(this)
                    .load(currentPhotoPath)
                    .transform(CenterCrop(), RoundedCorners(50) )
                    .into(binding.ibCamera)
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        val inputStream = contentResolver.openInputStream(uri)
        val outputDir = File(filesDir, "images")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "image_$timeStamp.jpg"
        val outputFile = File(outputDir, fileName)

        try {
            val outputStream = FileOutputStream(outputFile)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            return outputFile.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun deleteImageFile(filePath: String): Boolean {
        val file = File(filePath)
        return if (file.exists()) {
            file.delete().also {
                if (it) Log.e("삭제 성공", "파일이 삭제되었습니다: $filePath")
                else Log.e("삭제 실패", "파일을 삭제하지 못했습니다: $filePath")
            }
        } else {
            Log.e("파일 없음", "삭제할 파일이 존재하지 않습니다: $filePath")
            false
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            `currentPhotoPath` = absolutePath
            viewModel.upDateImage(currentPhotoPath)
        }
    }
}