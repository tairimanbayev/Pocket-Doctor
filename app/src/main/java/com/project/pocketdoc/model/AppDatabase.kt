package com.project.pocketdoc.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.pocketdoc.model.daos.*
import com.project.pocketdoc.model.tables.*

@Database(
    entities = [Profile::class, Fcm::class, Doctor::class, Card::class, Visit::class, VisitCard::class, Address::class, Clinic::class, Reason::class, VisitReason::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val profileDao: ProfileDao
    abstract val fcmDao: FcmDao
    abstract val doctorDao: DoctorDao
    abstract val cardDao: CardDao
    abstract val visitDao: VisitDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            if (!::INSTANCE.isInitialized) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE =
                        Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "app_db"
                        ).build()
                }
            }
            return INSTANCE
        }
    }
}