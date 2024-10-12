package com.nidhin.demo.data.api

import com.nidhin.demo.data.models.remote.GetAllProductsResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun getProducts( @Query("limit") limit: Int = 30, @Query("skip") skip: Int = 0): GetAllProductsResponse
//
//    @POST("app/store/getstoresconfig")
//    suspend fun getStoresConfig( @Body request: RequestBody): GetStoresConfigResponse
//
//    @POST("app/employee/login")
//    suspend fun storeLogin( @Body request: RequestBody) : StoreLoginResponse
//
//    @POST("app/store/refresh")
//    suspend fun storeRefresh( @Body request: RequestBody) : RefreshMenuResponse

}