package com.project.pocketdoc.repo

import android.content.Context
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.model.tables.Profile
import com.project.pocketdoc.network.requests.LoginRequest
import com.project.pocketdoc.util.apiCall
import com.project.pocketdoc.util.apiCallResponse
import com.project.pocketdoc.util.dbCall
import okhttp3.MultipartBody

class ProfileRepository(context: Context) : FcmRepository(context) {
    private val profileDao = database.profileDao
    private val doctorDao = database.doctorDao
    private val cardDao = database.cardDao
    private val preferences = context.getSharedPreferences("PREF", 0)

    suspend fun login(request: LoginRequest, onComplete: suspend (Profile) -> Unit) =
        apiCallResponse({ remoteApi.login(request) }, onComplete)

    suspend fun getProfileFromApi(fcmId: String, onComplete: suspend (Profile) -> Unit) =
        apiCallResponse({ remoteApi.getProfile(fcmId) }, onComplete)

    suspend fun createCard(fcmId: String, card: Card) =
        apiCallResponse({ remoteApi.createCard(fcmId, card) })

    suspend fun editCard(cardId: Int, fcmId: String, card: Card) =
        apiCallResponse({ remoteApi.editCard(cardId, fcmId, card) })

    suspend fun uploadPhoto(cardId: Int, photo: MultipartBody.Part?, fcm: MultipartBody.Part) =
        apiCall({ remoteApi.uploadImage(cardId, photo, fcm) })

    fun getLoggedProfileId() = preferences.getInt("LOGGED_USER_ID", -1)

    suspend fun getLoggedProfile() = dbCall {
        profileDao.getProfile(getLoggedProfileId())
    }

    suspend fun saveProfile(profile: Profile) = dbCall {
        preferences.edit().putInt("LOGGED_USER_ID", profile.id).apply()
        profileDao.insert(profile)
        profile.card?.let { cardDao.insert(it) }
        profile.doctor?.let {
            profile.doctorId = it.id
            doctorDao.insert(it)
        }
    }

    suspend fun getDoctor(id: Int) = dbCall { doctorDao.getDoctor(id) }

    suspend fun getCard(id: Int) = dbCall {
        val card = cardDao.getCard(id)
        card?.let { it.profile = profileDao.getProfile(it.userId) }
        card
    }

    suspend fun saveCard(card: Card) = dbCall { cardDao.insert(card) }
}