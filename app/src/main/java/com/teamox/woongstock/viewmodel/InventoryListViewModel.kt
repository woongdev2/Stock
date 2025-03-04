package com.teamox.woongstock.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.model.ProductTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InventoryListViewModel(private val databaseRepository: DatabaseRepository): ViewModel() {
    private val _fabClickEvent = MutableLiveData<Unit>()
    val fabClickEvent: LiveData<Unit> get() = _fabClickEvent
    private val _searchResultList = MutableLiveData<List<ProductTable>>()
    val searchResultList: LiveData<List<ProductTable>> get() = _searchResultList
    private val _items = MutableLiveData<List<ProductTable>>()
    val items: LiveData<List<ProductTable>> get() = _items
    lateinit var list: List<ProductTable>

    init { updateItem() }

    fun updateItem(){
        viewModelScope.launch(Dispatchers.IO) {
            val db = databaseRepository.getDatabase()
            list = db.productDao().getAll()
            _items.postValue(list)
        }
    }

    fun onFabBtnClick(){
        _fabClickEvent.value = Unit
    }

    fun searchFilterItems(searchText: String){
        if (::list.isInitialized) { // list가 초기화되었는지 확인
            _searchResultList.value = list.filter { item ->
                item.name.contains(searchText, ignoreCase = true)
            }
        } else {
            _searchResultList.value = emptyList() // list가 아직 초기화되지 않았을 경우 처리
        }
    }
}