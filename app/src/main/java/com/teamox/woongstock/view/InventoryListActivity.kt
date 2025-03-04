package com.teamox.woongstock.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamox.woongstock.R
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.databinding.ActivityInventoryListBinding
import com.teamox.woongstock.recyclerview.ProductListRecyclerview
import com.teamox.woongstock.viewmodel.InventoryListViewModel

class InventoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventoryListBinding
    private lateinit var viewModel: InventoryListViewModel
    private lateinit var mContext: Context
    private lateinit var rvProductListAdapter: ProductListRecyclerview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inventory_list)
        binding.lifecycleOwner = this
        viewModel = InventoryListViewModel(DatabaseRepository(this))
        binding.viewModel = viewModel
        mContext = this

        init()
    }

    private fun init(){
        setRecyclerView()
        setObserve()
        setSearch()
    }

    private fun setRecyclerView(){
        binding.rvProductList.layoutManager = LinearLayoutManager(mContext)
    }

    private fun setObserve(){
        viewModel.items.observe(this, Observer{
            rvProductListAdapter = ProductListRecyclerview(it, mContext)
            binding.rvProductList.adapter = rvProductListAdapter
        })

        viewModel.fabClickEvent.observe(this, Observer {
//            val intent = Intent(this, InventoryRegistrationActivity::class.java)
//            startActivity(intent)
            val intent = Intent(this, NavActivity::class.java)
            startActivity(intent)
        })

        viewModel.searchResultList.observe(this, Observer{
            rvProductListAdapter.setItem(it)
        })
    }

    private fun setSearch(){
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                viewModel.searchFilterItems(searchText)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                binding.etSearch.clearFocus()
                true
            } else {
                false
            }
        }
    }
}