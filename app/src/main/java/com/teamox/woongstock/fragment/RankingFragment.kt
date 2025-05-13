package com.teamox.woongstock.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.teamox.woongstock.R
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.databinding.FragmentRankingBinding
import com.teamox.woongstock.recyclerview.HistoryRecyclerview
import com.teamox.woongstock.viewmodel.HomeViewModel
import com.teamox.woongstock.viewmodel.RankingViewModel

class RankingFragment: Fragment() {
    private lateinit var viewModel: RankingViewModel
    private lateinit var mContext: Context
    private lateinit var binding: FragmentRankingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mContext = requireContext()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ranking, container, false)
        viewModel = RankingViewModel(DatabaseRepository(mContext))
        binding.viewModel = viewModel

        init()

        return binding.root
    }

    private fun init(){
        setObserve()
        setStockRanking()
    }

    private fun setStockRanking(){
        viewModel.getTop3()
    }

    private fun setObserve(){
        viewModel.list.observe(viewLifecycleOwner, Observer{
           Log.e("gmagma","${it}")
        })
    }
}