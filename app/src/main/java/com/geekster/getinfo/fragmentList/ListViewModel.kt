package com.geekster.getinfo.fragmentList


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekster.getinfo.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val listRepository: ListRepository) : ViewModel() {

    val listLiveData get() = listRepository.listLiveData
    val statusLiveData get() = listRepository.statusLiveData

    fun getList() {
        viewModelScope.launch {
            listRepository.getList()
        }
    }
}