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

    private var _marginRate = MutableLiveData<String>()
    val marginRate: LiveData<String> = _marginRate

    private val _btnLogisticsEvent = MutableLiveData<Unit>()
    val btnLogisticsEvent: LiveData<Unit> get() = _btnLogisticsEvent

    init {
        getDatabase()
    }

    fun getDatabase(){
        val db = databaseRepository.getDatabase().productDao()
        CoroutineScope(Dispatchers.IO).launch {
            val product= db.getProduct(productId)
            _itemData.postValue(product)
            if(product.purchasePrice.toDoubleOrNull() != null && product.sellingPrice.toDoubleOrNull() != null){
                setMarginData(product.purchasePrice.toDouble(), product.sellingPrice.toDouble())
            } else{
                _marginRate.postValue("-")
            }
        }
    }

    fun setMarginData(purchasePrice: Double, sellingPrice: Double){
        _marginRate.postValue(calculateMarginRate(purchasePrice, sellingPrice))
    }

    fun calculateMarginRate(purchasePrice: Double, sellingPrice: Double): String {
        val margin = sellingPrice - purchasePrice
        return String.format("%.1f", (margin / sellingPrice) * 100)
    }

    fun checkNullData(string: String): String{
        if(string.isNullOrBlank()){
            return "-"
        } else{
            return string
        }
    }

    fun onClickLogisticsBtn(){
        _btnLogisticsEvent.value = Unit
    }
}