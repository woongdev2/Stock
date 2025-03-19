package com.teamox.woongstock.viewmodel

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.model.ProductTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProductRegistrationViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {
    lateinit var _image: String
    val title = MutableLiveData<String>()
    val location = MutableLiveData<String>()
    val quantity = MutableLiveData<String>()
    val purchasePrice = MutableLiveData<String>()
    val sellingPrice = MutableLiveData<String>()
    val category = MutableLiveData<String>()
    val memo = MutableLiveData<String>()
    val _text = MutableLiveData<String>("Hello World")
    val text: LiveData<String> = _text


    fun upDateImage(image: String){
        _image = image
    }

    fun onClickButton(){
        if(title.value != null){
            inputProduct()
        } else{
            // Toast메시지 남겨주기?
        }
    }

    fun getTodayDate(): String {
        val today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return today.format(formatter)
    }

    private fun inputProduct(){
        if(!::_image.isInitialized) {
            _image = "-"
        }
        val id = databaseRepository.getPrimaryKey() + 1
        databaseRepository.setPrimaryKey(id)

        val product = ProductTable(
            id,
            title.value!!,
            _image,
            location.value ?: "",
            category.value ?:"",
            "",
            getTodayDate(),
            "",
            purchasePrice.value ?:"",
            sellingPrice.value ?:"",
            quantity.value ?:"",
            memo.value ?:"")

        CoroutineScope(Dispatchers.IO).launch {
            val db = databaseRepository.getDatabase().productDao()
            db.productRegistration(product)
            _text.postValue("ok")
        }
    }

}