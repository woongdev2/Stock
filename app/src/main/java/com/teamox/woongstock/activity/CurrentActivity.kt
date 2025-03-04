package com.teamox.woongstock.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import com.teamox.woongstock.adapter.CurrentItemRecyclerAdapter
import com.teamox.woongstock.R
import com.teamox.woongstock.common.CWFunction
import com.teamox.woongstock.data.CurrentItem
import com.teamox.woongstock.databinding.ActivityCurrentBinding
import com.teamox.woongstock.view.InventoryRegistrationActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class CurrentActivity : AppCompatActivity() {
    private val disposables = CompositeDisposable()
    lateinit var mContext: Context
    lateinit var binding: ActivityCurrentBinding

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 결과가 OK일 때 처리
            Log.e("test!!!!","$@@@@@@")
            val data: Intent? = result.data
            // 여기서 결과 처리
            Log.e("test!!!!","$@@@@@@")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_current)
        mContext = this

//        val intent = Intent(mContext, InventoryRegistrationActivity::class.java)
//        startActivity(intent)
        val intent = Intent(mContext, TestActivity::class.java)
        startActivity(intent)
//        init()
    }

    private fun init() {
        setRecyclerView()
        setOnClickEvent()
    }

    fun setRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = CWFunction.getDatabase(mContext).stockDao()
            val itemList = dao.getAll()
            for(index in itemList.indices) {
                Log.e("@@@@","${itemList[index].id}")
            }
            val rvAdapter = CurrentItemRecyclerAdapter(itemList, mContext)
            rvAdapter.notifyDataSetChanged()
            CoroutineScope(Dispatchers.Main).launch {
                binding.rvCurrent.adapter = rvAdapter
                binding.rvCurrent.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            }

            binding.etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val searchText = s.toString().trim()
                    val filteredList = itemList.filter { item ->
                        item.name.contains(searchText, ignoreCase = true)
                    }
                    rvAdapter.setItem(filteredList)
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                    binding.etSearch.clearFocus() // EditText에서 포커스를 해제합니다.
                    true
                } else {
                    false
                }
            }

            binding.btnSort.setOnClickListener {
//                val sortedList = itemList.sortedBy { it.price }
//                val sortedList = itemList.sortedByDescending { it.price }
//                rvAdapter.setItem(sortedList)
            }
        }
    }

    fun setOnClickEvent() {
        val btnAdd = binding.btnAdd.clicks().map { binding.btnAdd }
        val btnMore = binding.btnMore.clicks().map { binding.btnMore }

        val disposable = Observable.mergeArray(btnAdd, btnMore)
            .throttleFirst(500, TimeUnit.MICROSECONDS, AndroidSchedulers.mainThread())
            .subscribe {
                when (it) {
                    binding.btnAdd -> {
                        val intent = Intent(mContext, ChangeActivity::class.java)
                        getContent.launch(intent)
//                        startActivity(intent)
                    }

                    binding.btnMore -> {
                        //차후 모어 액티비티로 ㄲ
//                        val intent = Intent(mContext, ChangeActivity::class.java)
//                        startActivity(intent)
                    }
                }
            }

        disposables.add(disposable)
    }

}