package com.teamox.woongstock.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.model.ProductTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InventoryRegistrationViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {
    private val _text = MutableLiveData<String>("Hello World")
    val text: LiveData<String> = _text
    lateinit var _image: String
    val name = MutableLiveData<String>()
    val quantity = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    val _type = MutableLiveData<String>()
    val type: LiveData<String> = _type

    fun onClickButton(){
        if( name.value != null && quantity != null && price != null){
            inputProduct()
        } else{
            // Toast메시지 남겨주기?
        }
    }

    fun upDateImage(image: String){
        _image = image
    }

    fun validateInput(value: String): Boolean{
        return value.isEmpty()
    }

    private fun inputProduct(){
        if(!::_image.isInitialized) {
            _image = "-"
        }
        val id = databaseRepository.getPrimaryKey() + 1
        databaseRepository.setPrimaryKey(id)

        val product = ProductTable(
            id,
            name.value!!,
            _image,
            "",
            "",
            "",
            "",
            "",
            "",
            price.value!!,
            quantity.value!!,
            "")

        CoroutineScope(Dispatchers.IO).launch {
        val db = databaseRepository.getDatabase().productDao()
        db.productRegistration(product)
            _text.postValue("ok")
        }
    }
}