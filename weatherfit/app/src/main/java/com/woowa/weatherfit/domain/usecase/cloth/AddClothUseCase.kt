package com.woowa.weatherfit.domain.usecase.cloth

import android.content.Context
import android.net.Uri
import com.woowa.weatherfit.domain.model.Cloth
import com.woowa.weatherfit.domain.repository.ClothRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AddClothUseCase @Inject constructor(
    private val clothRepository: ClothRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(imageUri: Uri, cloth: Cloth): Result<Cloth> {
        val imageFile = uriToFile(imageUri)
        return clothRepository.registerClothesToServer(
            imageFile = imageFile,
            category = cloth.mainCategory,
            subCategory = cloth.subCategory
        )
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open URI: $uri")

        val file = File(context.cacheDir, "cloth_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        inputStream.close()
        return file
    }
}
