package com.hllbr.retrofit_compose.service

import com.hllbr.retrofit_compose.model.CryptoModel
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {

    @GET("prices?key=a82399be10cc5c1dadff681c8df1eeefc123916b")
    fun getData(): Call<List<CryptoModel>>
}