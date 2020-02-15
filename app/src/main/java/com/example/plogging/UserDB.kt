package com.example.plogging

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(User::class)],version = 1, exportSchema = false)
abstract class UserDB: RoomDatabase(){
    abstract fun userDao() : UserDao

    /* one and only one instance, similar to static in Java */
    companion object {
        private var sInstance: UserDB? = null
        @Synchronized
        fun get(context: Context): UserDB {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(context.applicationContext,
                    UserDB::class.java, "user.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return sInstance!!
        }
    }
}