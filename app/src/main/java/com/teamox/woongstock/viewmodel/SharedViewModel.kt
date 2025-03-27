package com.teamox.woongstock.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val _updatedQuantity = MutableLiveData<String>()
    val updatedQuantity: LiveData<String> = _updatedQuantity


    fun updateDataInDb(newQuantity: String) {
        Log.e("test",":$newQuantity")
        _updatedQuantity.postValue(newQuantity)
        Log.e("test", "_updatedQuantity.value set to: $newQuantity")
    }
}