package com.woowa.weatherfit.data.remote.api

import com.woowa.weatherfit.data.remote.dto.ClothesDetailResponse
import com.woowa.weatherfit.data.remote.dto.ClothesListResponse
import com.woowa.weatherfit.data.remote.dto.ClothesRegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ClothesApi {

    @Multipart
    @POST("clothes")
    suspend fun registerClothes(
        @Part image: MultipartBody.Part,
        @Part data: MultipartBody.Part
    ): ClothesDetailResponse

    @PUT("clothes/{clothesId}")
    suspend fun updateClothes(
        @Path("clothesId") clothesId: Long,
        @Body data: ClothesRegisterRequest
    ): ClothesDetailResponse

    @DELETE("clothes/{clothesId}")
    suspend fun deleteClothes(
        @Path("clothesId") clothesId: Long
    )

    @GET("clothes/{clothesId}")
    suspend fun getClothesDetail(
        @Path("clothesId") clothesId: Long
    ): ClothesDetailResponse

    @GET("clothes")
    suspend fun getClothesList(
        @Query("category") category: String? = null,
        @Query("sub") sub: String? = null,
        @Query("cursor") cursor: Long? = null,
        @Query("size") size: Int = 20
    ): ClothesListResponse
}
