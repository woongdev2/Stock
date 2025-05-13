package com.teamox.woongstock.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.teamox.woongstock.R
import com.teamox.woongstock.databinding.FragmentSettingsBinding
import com.teamox.woongstock.recyclerview.HistoryRecyclerview
import com.teamox.woongstock.viewmodel.HomeViewModel
import com.teamox.woongstock.viewmodel.SettingViewModel

class SettingsFragment: Fragment() {
    private lateinit var viewModel: SettingViewModel
    private lateinit var mContext: Context
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = requireContext()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        viewModel = SettingViewModel()
        binding.viewModel = viewModel

        init()

        return binding.root
    }

    private fun init(){
        setObserve()
    }

    private fun setObserve(){
        viewModel.openUrl.observe(viewLifecycleOwner, Observer{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        })
    }
}