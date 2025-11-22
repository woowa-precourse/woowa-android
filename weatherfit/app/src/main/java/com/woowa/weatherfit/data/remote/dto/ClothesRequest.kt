package com.woowa.weatherfit.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClothesRegisterRequest(
    @SerialName("category")
    val category: String,

    @SerialName("sub")
    val subCategory: String
)
