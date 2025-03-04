package com.teamox.woongstock.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamox.woongstock.R
import com.teamox.woongstock.common.CWFunction
import com.teamox.woongstock.databinding.ActivityProductDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailActivity: AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail)
        mContext = this

        init()
    }

    private fun init(){
        CoroutineScope(Dispatchers.IO).launch {
            val dao = CWFunction.getDatabase(mContext).stockDao()
            val data = dao.getDataWithPrimaryKey(intent.getIntExtra("db_key", 0))

            Log.e("data#$#$#", ":${data.name}")
        }

    }
}