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

class ItemDetailViewModel(private val databaseRepository: DatabaseRepository, private val productId: Int): ViewModel() {
    private var _itemData = MutableLiveData<ProductTable>()
    val itemData: LiveData<ProductTable> = _itemData

    init {
        getDatabase()
    }

    fun getDatabase(){
        val db = databaseRepository.getDatabase().productDao()
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("myLogGetDatabase","${db.getProduct(productId).name}")
            _itemData.postValue(db.getProduct(productId))
        }
    }
}