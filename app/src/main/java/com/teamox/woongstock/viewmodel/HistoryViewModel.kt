package com.teamox.woongstock.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.model.HistoryList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {
    private val _items = MutableLiveData<List<HistoryList>>()
    val items: LiveData<List<HistoryList>> get() = _items

    init {
        updateItem()
    }

    fun updateItem() {
        viewModelScope.launch(Dispatchers.IO) {
            val db = databaseRepository.getDatabase()
            val list = db.productDao().getHistoryList()
            _items.postValue(list)
        }
    }
}