package com.teamox.woongstock.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.teamox.woongstock.R
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.databinding.ActivityItemDetailBinding
import com.teamox.woongstock.viewmodel.ItemDetailViewModel

class ItemDetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityItemDetailBinding
    private lateinit var viewModel: ItemDetailViewModel
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var newQuantity: String
    private var productId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_detail)

        val intent = getIntent()
        productId = intent.getIntExtra("item_id", -1)
        viewModel = ItemDetailViewModel(DatabaseRepository(this), productId)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        init()
    }

    private fun init(){
        setObserve()
        setResultLauncher()
    }

    private fun setObserve(){
        viewModel.itemData.observe(this, Observer {
            Glide.with(this)
                .load(it.image)
                .transform(CenterCrop())
                .into(binding.ivProduct)
            binding.tvProductName.text = viewModel.checkNullData(it.name)
            binding.tvProductLocation.text = viewModel.checkNullData(it.location)
            binding.tvCategory.text = viewModel.checkNullData(it.category)
            binding.tvPurchasePrice.text = viewModel.checkNullData(it.purchasePrice)
            binding.tvSellingPrice.text = viewModel.checkNullData(it.sellingPrice)
            binding.tvStockDate.text = viewModel.checkNullData(it.stockDate)
            binding.tvShippingDate.text = viewModel.checkNullData(it.shippingDate)
            binding.tvProductQuantity.text = viewModel.checkNullData(it.quantity)
            binding.tvMemo.text = viewModel.checkNullData(it.memo)
        })

        viewModel.marginRate.observe(this, Observer {
            binding.tvMarginRate.text = it ?: "-"
        })

        viewModel.btnLogisticsEvent.observe(this, Observer {
            val intent = Intent(this, LogisticsActivity::class.java)
            intent.putExtra("item_id", productId)
            resultLauncher.launch(intent)
        })
    }

    private fun setResultLauncher(){
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                newQuantity = result.data?.getStringExtra("resultKey") ?: ""
                if(!newQuantity.isNullOrEmpty()) updateUI(newQuantity)
                setResult(RESULT_OK)
            }
        }
    }

    private fun updateUI(newData: String) {
        binding.tvProductQuantity.text = newData
    }
}