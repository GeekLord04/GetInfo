package com.geekster.getinfo.repository

import com.geekster.getinfo.api.ApiService
import com.geekster.getinfo.models.AddResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class AddRepository @Inject constructor(private val addAPI: ApiService) {

    suspend fun addProduct(
        productName: String,
        productType: String,
        price: String,
        tax: String,
        imageFile: File?
    ): AddResponse {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("product_name", productName)
            .addFormDataPart("product_type", productType)
            .addFormDataPart("price", price)
            .addFormDataPart("tax", tax)

        imageFile?.let {
            val fileBody = it.asRequestBody("image/*".toMediaTypeOrNull())
            requestBody.addFormDataPart("files[]", it.name, fileBody)
        }

        val request = addAPI.addProd(requestBody.build())
        if (!request.isSuccessful) {
            throw IOException("Error occurred during API call: ${request.message()}")
        }
        return request.body() ?: throw IOException("Response body is null")
    }
}
