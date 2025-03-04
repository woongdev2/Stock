package com.teamox.woongstock.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.teamox.woongstock.R
import com.teamox.woongstock.common.CWFunction
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.data.StockTable
import com.teamox.woongstock.databinding.ActivityInventoryRegistrationBinding
import com.teamox.woongstock.viewmodel.InventoryRegistrationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InventoryRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventoryRegistrationBinding
    private lateinit var viewModel: InventoryRegistrationViewModel
    private lateinit var mContext: Context
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inventory_registration)
        viewModel = InventoryRegistrationViewModel(DatabaseRepository(this))
        mContext = this

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        init()
    }

    private fun init(){
        setImageButton()
        setFocus()
        setClickCameraBtn()
        setObserve()
    }

    private fun setImageButton(){
        Glide.with(this)
            .load(R.drawable.camera)
            .transform(CenterCrop(), RoundedCorners(50) )
            .into(binding.ibCamera)
    }

    private fun setFocus(){
        binding.etTitle.post {
            binding.etTitle.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etTitle, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun setObserve(){
        viewModel.text.observe( this) {
            if(it.equals("ok")){
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        viewModel.type.observe(this, Observer { type ->
//            binding.ibCamera.visibility = View.VISIBLE
            if(type.equals("ok")){
                binding.btnConfirm
            } else{

            }
        })


        binding.etTitle.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                if(!viewModel.validateInput(binding.etTitle.text.toString())){
                    okView(binding.textInputLayoutPrice, "제품 가격을\n입력해주세요","비싸면 안사요")
                    binding.etPrice.requestFocus()
                } else{

                }
                true
            } else {
                false
            }
        }

        binding.etPrice.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                if(!viewModel.validateInput(binding.etPrice.text.toString())){
                    okView(binding.textInputLayoutQuantity, "제품 수량을\n입력해주세요", "많으면 안사요")
                    binding.etQuantity.requestFocus()
                } else{

                }

                true
            } else {
                false
            }
        }


        binding.etQuantity.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                if(!viewModel.validateInput(binding.etQuantity.text.toString())){
                    okView(binding.ibCamera, "제품 수량을\n입력해주세요","많으면 안사요22")
                    hideKeyboard()
                    binding.btnConfirm.visibility = View.VISIBLE
                } else{

                }
                true // 이벤트 처리 완료
            } else {
                false // 이벤트 처리되지 않음
            }
        }
    }

    private fun okView(view: View, text: String, subText: String){
        view.visibility = View.VISIBLE
        binding.tvIntroTitle.text = text
        binding.tvIntroSubTitle.text = subText
    }

    fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0) // 키보드 숨기기
        }
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
            Log.e("여기가","크레이트${currentPhotoPath}")
            viewModel.upDateImage(currentPhotoPath)
        }
    }
}