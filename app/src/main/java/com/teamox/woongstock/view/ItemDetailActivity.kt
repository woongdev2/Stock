package com.teamox.woongstock.view

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_detail)

        val intent = getIntent()
        viewModel = ItemDetailViewModel(DatabaseRepository(this), intent.getIntExtra("item_id", -1))

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setObserve()
        //뼈대 작업할떄까지만 잠시
//        viewModel.itemData.observe(this, Observer { product ->
//            binding.tvProductName.text = product.name
//            binding.tvProductPrice.text = reSize(product.price +"원", 0.8f, product.price.length)
//            binding.tvProductQuantity.text = reSize(product.quantity +"개", 0.8f, product.quantity.length)
//            Glide.with(this)
//                .load(product.image)
//                .transform(CenterCrop())
//                .into(binding.ivProduct)
//        })
    }

    fun setObserve(){
        viewModel.itemData.observe(this, Observer {
            Glide.with(this)
                .load(it.image)
                .transform(CenterCrop())
                .into(binding.ivProduct)
            binding.tvProductName.text = it.name
            binding.tvProductLocation.text = it.location ?: "-"
            binding.tvCategory.text = it.category ?: "-"
            binding.tvPurchasePrice.text = it.purchasePrice ?: "-"
            binding.tvSellingPrice.text = it.sellingPrice ?: "-"
            binding.tvStockDate.text = it.stockDate ?: "-"
            binding.tvShippingDate.text = it.shippingDate ?: "-"
            binding.tvProductQuantity.text = it.quantity
        })
    }

    fun reSize(text: String, size: Float, start: Int): SpannableString{
        val spannableString = SpannableString(text)
        spannableString.setSpan(RelativeSizeSpan(size), start, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableString
    }
}