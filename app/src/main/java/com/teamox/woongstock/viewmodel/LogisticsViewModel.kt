package com.teamox.woongstock.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamox.woongstock.common.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogisticsViewModel(private val databaseRepository: DatabaseRepository, private val productId: Int): ViewModel() {
    val _quantity = MutableLiveData<String>("0")
    var quantity: LiveData<String> = _quantity
    var date = MutableLiveData<String>()
    var type = "in"
    var client = MutableLiveData<String>()
    var memo = MutableLiveData<String>()
    private val _finishEvent = MutableLiveData<Boolean>()
    val finishEvent: LiveData<Boolean> get() = _finishEvent

    fun onClickPlus(){
       _quantity.value = (quantity.value!!.toInt() + 1).toString()
    }

    fun onClickMinus(){
        _quantity.postValue((quantity.value!!.toInt() - 1).toString())
    }

    fun onClickConfirm(){
        viewModelScope.launch(Dispatchers.IO) {
            val newQuantity = databaseRepository.getDatabase().productDao().getProduct(productId).quantity.toInt() + quantity.value!!.toInt()
            databaseRepository.
            updateQuantityAndLog(productId, newQuantity.toString(),date.value?:"",type,client.value?:"",memo.value?:"",quantity.value!!)
            withContext(Dispatchers.Main) {
                _finishEvent.value = true
            }
        }
    }
}