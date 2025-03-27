package com.teamox.woongstock.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamox.woongstock.R
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.databinding.FragmentHistoryBinding
import com.teamox.woongstock.recyclerview.HistoryRecyclerview
import com.teamox.woongstock.recyclerview.ProductListRecyclerview
import com.teamox.woongstock.view.ItemDetailActivity
import com.teamox.woongstock.viewmodel.HistoryViewModel
import com.teamox.woongstock.viewmodel.HomeViewModel
import com.teamox.woongstock.viewmodel.InventoryListViewModel

class HistoryFragment: Fragment() {
    private lateinit var viewModel: HistoryViewModel
    private lateinit var mContext: Context
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var rvHistoryAdapter: HistoryRecyclerview

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mContext = requireContext()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        viewModel = HistoryViewModel(DatabaseRepository(mContext))
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        setRecyclerView()
        setObserve()
    }

    private fun setRecyclerView(){
        binding.rvHistory.layoutManager = LinearLayoutManager(mContext)
    }

    private fun setObserve(){
        viewModel.items.observe(viewLifecycleOwner, Observer{
            rvHistoryAdapter = HistoryRecyclerview(mContext, it)

            binding.rvHistory.adapter = rvHistoryAdapter
        })

    }


}