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
    val _increment = MutableLiveData<String>("0")
    var increment: LiveData<String> = _increment
    var date = MutableLiveData<String>("현재 시간")
    var type = "in"
    var client = MutableLiveData<String>("작성")
    var memo = MutableLiveData<String>("작성")
    private val _finishEvent = MutableLiveData<String>()
    val finishEvent: LiveData<String> = _finishEvent

    fun onClickPlus(){
       _increment.value = (increment.value!!.toInt() + 1).toString()
    }

    fun onClickMinus(){
        _increment.postValue((increment.value!!.toInt() - 1).toString())
    }

    fun onClickConfirm(){
        viewModelScope.launch(Dispatchers.IO) {
            val existingQuantity = databaseRepository.getDatabase().productDao().getProduct(productId).quantity.toInt()
            val newQuantity = existingQuantity + increment.value!!.toInt()
            databaseRepository.
            updateQuantityAndLog(productId, newQuantity.toString(),date.value?:"",type,client.value?:"",memo.value?:"",existingQuantity.toString()?:"", increment.value?:"")
            withContext(Dispatchers.Main) {
                _finishEvent.value = newQuantity.toString()
            }
        }
    }
}