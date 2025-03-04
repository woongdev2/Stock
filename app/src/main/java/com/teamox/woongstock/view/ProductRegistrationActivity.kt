package com.teamox.woongstock.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamox.woongstock.R
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.databinding.ActivityProductRegistrationBinding
import com.teamox.woongstock.viewmodel.ProductRegistrationViewModel

class ProductRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductRegistrationBinding
    private lateinit var viewModel: ProductRegistrationViewModel
    private lateinit var mContext: Context
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_registration)
        viewModel = ProductRegistrationViewModel(DatabaseRepository(this))

        mContext = this

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        init()
    }

    private fun init(){

    }

}