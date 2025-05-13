package com.teamox.woongstock.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel: ViewModel() {
    private val _openUrl = MutableLiveData<String>()
    val openUrl: LiveData<String> = _openUrl

    private val urlMap = mapOf(
        "notice" to "https://luck-blender-abb.notion.site/1c58caf34623806b874ff90924e6678a?pvs=4",
        "guide" to "https://luck-blender-abb.notion.site/1c58caf3462380039240c67ef511d5ad?pvs=4"
    )

    fun onLinkClick(buttonId: String) {
        val url = urlMap[buttonId]
        if (url != null) _openUrl.value = url!!
    }
}