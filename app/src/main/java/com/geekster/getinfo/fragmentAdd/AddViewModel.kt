package com.geekster.getinfo.fragmentAdd

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.geekster.getinfo.NetworkResult
import com.geekster.getinfo.models.AddResponse
import com.geekster.getinfo.repository.AddRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(private val repository: AddRepository) : ViewModel() {

    fun addProduct(
        productName: String,
        productType: String,
        price: String,
        tax: String,
        imageFile: File?
    ): LiveData<NetworkResult<AddResponse>> {
        return liveData(Dispatchers.IO) {
            emit(NetworkResult.Loading())

            try {
                val response = repository.addProduct(
                    productName,
                    productType,
                    price,
                    tax,
                    imageFile
                )
                emit(NetworkResult.Success(response))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message ?: "Error Occurred!", null))
            }
        }
    }
}
