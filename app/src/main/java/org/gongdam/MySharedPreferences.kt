package org.gongdam

import android.content.Context
import android.content.SharedPreferences

private const val FILENAME = "prefs"
private const val PREF_TEXT = "ID"
private const val PREF_TEXT2 = "PW"

class MySharedPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, 0)
    var text_id:String
        get() = prefs.getString(PREF_TEXT, "")
        set(value) = prefs.edit().putString(PREF_TEXT, value).apply()

    var text_pw:String
        get() = prefs.getString(PREF_TEXT2, "")
        set(value) = prefs.edit().putString(PREF_TEXT2, value).apply()
}