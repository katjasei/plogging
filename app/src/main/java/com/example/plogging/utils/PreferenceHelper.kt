package com.example.plogging.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {

    private const val USER_EMAIL = "USER_EMAIL"
    private const val USER_PASSWORD = "PASSWORD"
    private const val DISTANCE = "DISTANCE"
    private const val DURATION = "DURATION"

    fun customPreference(context: Context, name: String):
            SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    private fun SharedPreferences.Editor.put(pair: Pair<String, Any>) {
        val key = pair.first
        when (val value = pair.second) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitive types can be stored in SharedPreferences")
        }
    }

    var SharedPreferences.userEmail
        get() = getString(USER_EMAIL, "")
        set(value) {
            editMe {
                it.put(USER_EMAIL to value)
                //it.putInt(USER_ID, value)
                }
        }
        var SharedPreferences.password
            get() = getString( USER_PASSWORD, "")
            set(value) {
                editMe {
                    it.put(USER_PASSWORD to value)
                    //it.putString(USER_PASSWORD, value)
                    }
            }
            var SharedPreferences.clearValues
                get() = {}
                set(_) {
                    editMe {
                        it.remove(USER_EMAIL)
                        it.remove(USER_PASSWORD)
                  }
                }

    var SharedPreferences.distance
        get() = getString( DISTANCE, "")
        set(value) {
            editMe {
                it.put(DISTANCE to value)

            }
        }

    var SharedPreferences.duration
        get() = getString( DURATION, "")
        set(value) {
            editMe {
                it.put(DURATION to value)
            }
        }

}

