package com.project.pocketdoc.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.repo.ProfileRepository
import com.project.pocketdoc.util.FCM_PROBLEM
import com.project.pocketdoc.util.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class EditProfileViewModel(private val repository: ProfileRepository, private val cardId: Int) : StatusViewModel<Boolean>() {
    private val _card = MutableLiveData<Card>()
    val card: LiveData<Card> by lazy {
        viewModelScope.launch {
            _card.value = if (cardId == 0) Card() else repository.getCard(cardId) ?: Card()
        }
        _card
    }

    fun saveProfile(card: Card, bitmap: Bitmap?) {
        _status.value = Status.Loading
        viewModelScope.launch {
            val fcm = repository.getFcm()
            _status.value = if (fcm == null) Status.Failure(FCM_PROBLEM) else {
                val response = if (card.id != 0)
                    repository.editCard(cardId, fcm.fcmId, card)
                else
                    repository.createCard(fcm.fcmId, card)
                if (response is Status.Complete) {
                    Log.d("EditProfileVMLogcat", "saveProfile: ${response.result}")
                    repository.saveCard(response.result)
                    if (bitmap != null)
                        uploadPhoto(bitmap, fcm.fcmId)
                    else
                        Status.Complete(true)
                } else Status.Failure((response as Status.Failure).message)
            }
        }
    }

    private suspend fun uploadPhoto(bitmap: Bitmap, fcmId: String) = withContext(Dispatchers.Default) {
        val scaledBitmap = Bitmap.createScaledBitmap(cropBitmap(bitmap), 512, 512, false)
        val outputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        val bytes = outputStream.toByteArray()
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes)
        val photo = if (bytes.isNotEmpty()) MultipartBody.Part.createFormData("photo", "photo.jpg", requestFile)
        else null
        val fcm = MultipartBody.Part.createFormData("fcm", fcmId)
        val response = repository.uploadPhoto(cardId, photo, fcm)
        Status.Complete(response is Status.Complete)
    }

    private fun cropBitmap(src: Bitmap) = if (src.height > src.width) {
        Bitmap.createBitmap(src, 0, (src.height - src.width) / 2, src.width, src.width)
    } else {
        Bitmap.createBitmap(src, (src.width - src.height) / 2, 0, src.height, src.height)
    }
}
