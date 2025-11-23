package com.woowa.weatherfit.data.remote.api

import com.woowa.weatherfit.data.remote.dto.OutfitDetailResponse
import com.woowa.weatherfit.data.remote.dto.OutfitResponse
import com.woowa.weatherfit.data.remote.dto.OutfitsListResponse
import com.woowa.weatherfit.data.remote.dto.UpdateOutfitRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface OutfitApi {

    @Multipart
    @POST("outfits")
    suspend fun createOutfit(
        @Part thumbnail: MultipartBody.Part,
        @Part("clothes") clothes: RequestBody,
        @Part("category") category: RequestBody
    ): OutfitResponse

    @GET("outfits")
    suspend fun getOutfits(): OutfitsListResponse

    @GET("outfits/{id}")
    suspend fun getOutfitDetail(
        @Path("id") id: Long
    ): OutfitDetailResponse

    @PUT("outfits/{id}")
    suspend fun updateOutfit(
        @Path("id") id: Long,
        @Body request: UpdateOutfitRequest
    ): OutfitResponse

    @DELETE("outfits/{id}")
    suspend fun deleteOutfit(
        @Path("id") id: Long
    )

    @PATCH("outfits/{id}/fixed")
    suspend fun toggleFixed(
        @Path("id") id: Long
    ): OutfitResponse
}
