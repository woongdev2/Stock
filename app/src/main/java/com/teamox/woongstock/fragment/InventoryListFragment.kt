package com.teamox.woongstock.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamox.woongstock.R
import com.teamox.woongstock.common.CWFunction
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.databinding.FragmentInventoryListBinding
import com.teamox.woongstock.recyclerview.ProductListRecyclerview
import com.teamox.woongstock.view.InventoryRegistrationActivity
import com.teamox.woongstock.view.ItemDetailActivity
import com.teamox.woongstock.view.ProductRegistrationActivity
import com.teamox.woongstock.viewmodel.InventoryListViewModel

class InventoryListFragment: Fragment(){

    private lateinit var binding: FragmentInventoryListBinding
    private lateinit var viewModel: InventoryListViewModel
    private lateinit var mContext: Context
    private lateinit var rvProductListAdapter: ProductListRecyclerview
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.updateItem()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = requireContext()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inventory_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = InventoryListViewModel(DatabaseRepository(mContext))
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onStart() {
        super.onStart()
        binding.etSearch.text.clear()
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
        viewModel.items.observe(viewLifecycleOwner, Observer{
            rvProductListAdapter = ProductListRecyclerview(it, mContext){ item ->
                val intent = Intent(mContext, ItemDetailActivity::class.java)
                intent.putExtra("item_id", item)
                launcher.launch(intent)
            }

            binding.rvProductList.adapter = rvProductListAdapter
        })

        viewModel.fabClickEvent.observe(viewLifecycleOwner, Observer {
            val intent = Intent(requireContext(), ProductRegistrationActivity::class.java)
            launcher.launch(intent)
        })

        viewModel.searchResultList.observe(viewLifecycleOwner, Observer{
            if(!it.isEmpty()){
                rvProductListAdapter.setItem(it)
            }
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
                val inputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                binding.etSearch.clearFocus()
                true
            } else {
                false
            }
        }
    }

    override fun onStop() {
        super.onStop()
//        if(!binding.etSearch.text.equals("")){
//            binding.etSearch.text = null
//        }
        /**
         * 다른 프레그먼트로 이동했을 때
         * onPause
         * onStop
         * onDestroyView 순까지. 다시 클릭했을때 위 코드를 실행할지는 생각해보자.
         */
    }
}