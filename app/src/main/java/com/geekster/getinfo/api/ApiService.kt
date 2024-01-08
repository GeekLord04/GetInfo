package com.geekster.getinfo.api

import com.geekster.getinfo.models.AddResponse
import com.geekster.getinfo.models.ListResponse
import com.geekster.getinfo.models.ListResponseItem
import com.geekster.getinfo.utils.Constants.addAPI
import com.geekster.getinfo.utils.Constants.listAPI
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET(listAPI)
    suspend fun getInfo(): Response<List<ListResponseItem>>

    @POST(addAPI)
    suspend fun addProd(@Body requestBody: RequestBody) : Response<AddResponse>

//        @GET(addAPI)
//        suspend fun getCryptoInfo(): Response<CryptoResponse>
    }