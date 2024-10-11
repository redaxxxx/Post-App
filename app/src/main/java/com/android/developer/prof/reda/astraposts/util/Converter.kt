package com.android.developer.prof.reda.astraposts.util

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream

fun getImageMultipart(imageUri: Uri, contentResolver: ContentResolver): MultipartBody.Part{
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(imageUri, filePathColumn, null,null,null)

    cursor?.moveToFirst()

    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
    val picturePath = cursor?.getString(columnIndex?: 0)

    cursor?.close()

    val file = File(picturePath?: "")
    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)

    return MultipartBody.Part.createFormData("post_image", file.name, requestFile)
}

fun getUpdateImageMultipart(imageUri: Uri, contentResolver: ContentResolver): MultipartBody.Part? {
    return try {
        // Attempt to get file name from OpenableColumns
        var fileName: String? = null
        val cursor = contentResolver.query(imageUri, null, null, null, null)
        cursor?.use {
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    fileName = cursor.getString(nameIndex)
                }
            }
        }

        // If fileName is still null, extract from URI
        if (fileName == null) {
            fileName = imageUri.lastPathSegment ?: "unknown_image"
        }

        // Open InputStream for reading the file
        val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
        val tempFile = createTempFile("upload", fileName)

        // Write the InputStream to the temporary file
        inputStream?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        // Create a RequestBody for the file
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), tempFile)
        MultipartBody.Part.createFormData("post_image", tempFile.name, requestFile)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun createPartFromString(text: String): RequestBody{
    return RequestBody.create("text/plain".toMediaTypeOrNull(), text)
}

fun createPartFromInt(id: Int): RequestBody {
    return RequestBody.create("text/plain".toMediaTypeOrNull(), id.toString())
}