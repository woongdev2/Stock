package com.teamox.woongstock.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.teamox.woongstock.R
import com.teamox.woongstock.databinding.ActivityFirstTestBinding
import com.teamox.woongstock.viewmodel.SharedViewModel

class TestFirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstTestBinding
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_first_test)
        testFunction()
    }

    fun testFunction(){
        binding.btnTest.setOnClickListener {
            val intent = Intent(this, TestSecondActivity::class.java)
            startActivity(intent)
        }

        sharedViewModel.updatedQuantity.observe(this, Observer { newQuantity ->
            // UI 업데이트
            Log.e("test", "Updated data: $newQuantity")
        })
    }
}