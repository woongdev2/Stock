package com.teamox.woongstock.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jakewharton.rxbinding3.view.clicks
import com.teamox.woongstock.R
import com.teamox.woongstock.common.CWFunction
import com.teamox.woongstock.databinding.ActivitySelectBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SelectActivity : AppCompatActivity() {
    private val disposables = CompositeDisposable()
    lateinit var binding: ActivitySelectBinding
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select)
        mContext = this
        init()
    }

    private fun init(){
        val intent = Intent(mContext, ChangeActivity::class.java)
        startActivity(intent)
        setClickEvent()
    }

    fun setClickEvent(){
        val btnCurrent = binding.btnCurrent.clicks().map{binding.btnCurrent}
        val btnChange  = binding.btnChange.clicks().map{binding.btnChange}

        val disposable = Observable.mergeArray(btnCurrent, btnChange)
            .throttleFirst( 500, TimeUnit.MICROSECONDS, AndroidSchedulers.mainThread())
            .subscribe {
                when(it) {
                    binding.btnCurrent -> {
                        val intent = Intent(mContext, CurrentActivity::class.java)
                        startActivity(intent)
                    }

                    binding.btnChange -> {
                        val intent = Intent(mContext, ChangeActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

        disposables.add(disposable)
    }
}