package com.teamox.woongstock.view

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.widget.Toast
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
    private var productId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_detail)

        val intent = getIntent()
        productId = intent.getIntExtra("item_id", -1)
        viewModel = ItemDetailViewModel(DatabaseRepository(this), productId)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setObserve()
    }

    fun setObserve(){
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
            startActivity(intent)
        })

        viewModel.allProduct.observe(this) { products ->
            updateUI()
        }
    }

    private fun updateUI() {
        // 리스트 새로고침
        Toast.makeText(this, "앱을 찾을 수 없습니다: $packageName", Toast.LENGTH_SHORT).show()
    }

    fun setData(string: String): String{
        return viewModel.checkNullData(string)
    }

    fun reSize(text: String, size: Float, start: Int): SpannableString{
        val spannableString = SpannableString(text)
        spannableString.setSpan(RelativeSizeSpan(size), start, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableString
    }
}