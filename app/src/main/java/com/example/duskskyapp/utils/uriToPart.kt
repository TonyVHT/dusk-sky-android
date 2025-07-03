package com.example.duskskyapp.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

fun uriToPart(context: Context, uri: Uri?, partName: String): MultipartBody.Part? {
    if (uri == null) return null
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri) ?: return null
    val fileName = "file_${System.currentTimeMillis()}"
    val bytes = inputStream.readBytes()
    inputStream.close()
    val type = contentResolver.getType(uri) ?: "image/*"
    val reqFile = RequestBody.create(type.toMediaTypeOrNull(), bytes)
    return MultipartBody.Part.createFormData(partName, fileName, reqFile)
}
