package com.project.pocketdoc.viewmodels

import androidx.lifecycle.viewModelScope
import com.project.pocketdoc.model.tables.Profile
import com.project.pocketdoc.repo.ProfileRepository
import com.project.pocketdoc.util.FCM_PROBLEM
import com.project.pocketdoc.util.Status
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : LazyViewModel<Profile>() {
    private val TAG = "ProfileViewModelLogcat"

    override fun loadData() {
        viewModelScope.launch {
            val fcm = repository.getFcm()
            if (fcm == null) {
                _status.value = Status.Failure(FCM_PROBLEM)
                return@launch
            }
            getFromDb(fcm.fcmId)
            getFromApi(fcm.fcmId)
        }

    }

    private suspend fun getFromDb(fcmId: String) = viewModelScope.launch {
        repository.getLoggedProfile()?.let {
            if (it.doctorId != null) {
                val doctor = repository.getDoctor(it.doctorId!!)
                it.doctor = doctor
            }
            if (it.cardId != null) {
                val card = repository.getCard(it.cardId!!)?.also { card -> card.fcmId = fcmId }
                it.card = card
            }
            _status.value = Status.Complete(it)
        }
    }

    private suspend fun getFromApi(fcmId: String) = viewModelScope.launch {
        _status.value = repository.getProfileFromApi(fcmId) { profile ->
            profile.card?.fcmId = fcmId
            profile.doctor?.let { profile.doctorId = it.id }
            saveToDb(profile)
        }
    }

    private suspend fun saveToDb(profile: Profile) = viewModelScope.launch {
        repository.saveProfile(profile)
    }
}
