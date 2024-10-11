package com.android.developer.prof.reda.astraposts.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
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

fun getUpdateImageMultipart(imageUri: Uri, contentResolver: ContentResolver, context: Context): MultipartBody.Part? {
    return try {
        // Get file name
        val fileName = getFileNameFromUri(imageUri, contentResolver)

        // Open InputStream for reading the file
        val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
        val tempFile = File.createTempFile("upload", fileName, context.cacheDir)

        // Write InputStream to the temporary file
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

fun getFileNameFromUri(uri: Uri, contentResolver: ContentResolver): String {
    var fileName: String? = null
    val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0) {
                fileName = it.getString(nameIndex)
            }
        }
    }

    // Fallback to extracting file name from URI if the cursor is empty
    return fileName ?: uri.lastPathSegment ?: "unknown_file"
}

fun createPartFromString(text: String): RequestBody{
    return RequestBody.create("text/plain".toMediaTypeOrNull(), text)
}

fun createPartFromInt(id: Int): RequestBody {
    return RequestBody.create("text/plain".toMediaTypeOrNull(), id.toString())
}