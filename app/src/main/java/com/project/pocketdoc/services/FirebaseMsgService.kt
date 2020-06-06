package com.project.pocketdoc.services

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.project.pocketdoc.model.AppDatabase
import com.project.pocketdoc.model.tables.Fcm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirebaseMsgService : FirebaseMessagingService() {

    private val TAG = "FirebaseServiceLogcat"

    init {
        Log.d(TAG, "init: ")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("messageBody", remoteMessage.data.toString())
        Log.e("messageBody", remoteMessage.notification?.body.toString())
        Log.e("messageTitle", remoteMessage.notification?.title.toString())
        Log.e("messageType", remoteMessage.data["type"] ?: "null")
    }

    override fun onNewToken(token: String) {
        saveToken(token)
        Log.d(TAG, "onNewToken: ")
        super.onNewToken(token)
    }

    private fun saveToken(token: String) = CoroutineScope(Dispatchers.IO).launch {
        val fcmDao = AppDatabase.getInstance(applicationContext).fcmDao
        val fcm = fcmDao.getFcm() ?: Fcm()
        fcm.fcmId = token
        fcmDao.insert(fcm)
    }

    companion object {
        fun getToken(context: Context, onComplete: (String) -> Unit) = CoroutineScope(Dispatchers.IO).launch {
            val fcmDao = AppDatabase.getInstance(context.applicationContext).fcmDao
            val fcm = fcmDao.getFcm()
            if (fcm != null)
                withContext(Dispatchers.Main) {
                    onComplete(fcm.fcmId)
                }
        }
    }
}