package com.teamox.woongstock.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.model.ProductIncrement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RankingViewModel(private val databaseRepository: DatabaseRepository): ViewModel() {
    private val _list = MutableLiveData<List<ProductIncrement>>()
    val list: LiveData<List<ProductIncrement>> get() = _list

    fun getTop3(){
        viewModelScope.launch(Dispatchers.IO) {
            val db = databaseRepository.getDatabase()
            val list = db.productDao().getTop3ProductsByIncrementInLastWeek()
            _list.postValue(list)
        }
    }
}