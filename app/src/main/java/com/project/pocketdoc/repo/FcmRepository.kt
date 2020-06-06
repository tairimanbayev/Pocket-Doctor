package com.project.pocketdoc.repo

import android.content.Context
import com.project.pocketdoc.model.AppDatabase
import com.project.pocketdoc.network.ApiFactory
import com.project.pocketdoc.util.dbCall

open class FcmRepository(context: Context) {
    protected val remoteApi = ApiFactory.apiService
    protected val database = AppDatabase.getInstance(context)
    private val fcmDao = database.fcmDao

    suspend fun getFcm() = dbCall { fcmDao.getFcm() }
}