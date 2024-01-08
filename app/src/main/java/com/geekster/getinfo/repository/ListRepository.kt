package com.geekster.getinfo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.geekster.getinfo.NetworkResult
import com.geekster.getinfo.api.ApiService
import com.geekster.getinfo.models.ListResponseItem
import com.geekster.getinfo.utils.Constants.TAG
import org.json.JSONObject
import javax.inject.Inject

class ListRepository @Inject constructor(private val listAPI : ApiService) {

    private val _listLiveData = MutableLiveData<NetworkResult<List<ListResponseItem>>>()
    val listLiveData : LiveData<NetworkResult<List<ListResponseItem>>>
        get() = _listLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData : LiveData<NetworkResult<String>>
        get() = _statusLiveData


    suspend fun getList(){
        _listLiveData.postValue(NetworkResult.Loading())
        val response = listAPI.getInfo()
        if (response.isSuccessful && response.body() != null) {
            _listLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _listLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _listLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }

//        Log.d(TAG, "getList: $response")
    }
}