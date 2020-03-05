package com.example.plogging.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferenceHelper {

    val USER_EMAIL = "USER_EMAIL"
    val USER_PASSWORD = "PASSWORD"
    fun defaultPreference(context: Context):
            SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String):
            SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    private fun SharedPreferences.Editor.put(pair: Pair<String, Any>) {
        val key = pair.first
        val value = pair.second
        when (value) {
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
                set(value) {
                    editMe {
                        it.remove(USER_EMAIL)
                        it.remove(USER_PASSWORD)
                  }
                }
}

