package com.teamox.woongstock.activity

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Entity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jakewharton.rxbinding3.view.clicks
import com.teamox.woongstock.R
import com.teamox.woongstock.common.CWFunction
import com.teamox.woongstock.data.StockTable
import com.teamox.woongstock.databinding.ActivityChangeBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ChangeActivity : AppCompatActivity() {
    private val disposables = CompositeDisposable()
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var binding: ActivityChangeBinding
    lateinit var mContext: Context
    lateinit var currentPhotoPath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change)
        mContext = this

        init()
    }

    private fun init(){
        setClickListener()
        Glide.with(this)
            .load(R.drawable.camera)
            .transform(CenterCrop(), RoundedCorners(50) )
            .into(binding.ibCamera)
    }

    private fun setClickListener(){
        val ibCamera = binding.ibCamera.clicks().map { binding.ibCamera }
        val btnConfirm = binding.btnConfirm.clicks().map{binding.btnConfirm}

        val disposable = Observable.mergeArray(ibCamera, btnConfirm)
            .throttleFirst(500, TimeUnit.MICROSECONDS, AndroidSchedulers.mainThread())
            .subscribe {
                when (it) {
                    binding.ibCamera -> {
                        dispatchTakePictureIntent()
                    }

                    binding.btnConfirm -> {
                        saveData()
                    }
                }
            }

        disposables.add(disposable)
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
                    .centerCrop()
                    .into(binding.ibCamera)
                currentPhotoPath = saveImageToInternalStorage(uri)!!
            } ?: run{
                Glide.with(this)
                    .load(currentPhotoPath)
                    .centerCrop()
                    .into(binding.ibCamera)
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        val inputStream = contentResolver.openInputStream(uri)
        val outputDir = File(filesDir, "images") // 이미지를 저장할 디렉토리 생성
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "image_$timeStamp.jpg" // 이미지 파일명 설정
        val outputFile = File(outputDir, fileName)

        try {
            val outputStream = FileOutputStream(outputFile)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            return outputFile.absolutePath // 파일의 절대 경로 반환
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
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
            currentPhotoPath = absolutePath
        }
    }

    fun saveData(){
        CoroutineScope(Dispatchers.IO).launch {
            val dao = CWFunction.getDatabase(mContext).stockDao()

            val id = CWFunction.getIntSharedPref(mContext, "databasePref", "stockId") + 1
            CWFunction.setIntSharedPref(mContext, "databasePref", "stockId", id)

            Log.e("@@@@","추가된index=${id}")

            val product = StockTable(id,
                binding.etTitle.text.toString(),
                currentPhotoPath,
                binding.etPrice.text.toString(),
                binding.etQuantity.text.toString())

            dao.insertAll(product)

            setResult(RESULT_OK,intent)
            finish()
        }
    }

}