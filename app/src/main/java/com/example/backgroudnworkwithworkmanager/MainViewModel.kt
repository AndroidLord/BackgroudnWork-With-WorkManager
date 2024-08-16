package com.example.backgroudnworkwithworkmanager

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.UUID

class MainViewModel : ViewModel() {

    var uri: Uri? by mutableStateOf(null)
        private set

    var compressedBitmap: Bitmap? by mutableStateOf(null)
        private set

    var workId: UUID? by mutableStateOf(null)
        private set

    fun updateCompressedBitmap(bitmap: Bitmap) {
        compressedBitmap = bitmap
    }

    fun updateWorkId(id: UUID) {
        workId = id
    }

    fun updateUri(uri: Uri?) {
        this.uri = uri
    }

}