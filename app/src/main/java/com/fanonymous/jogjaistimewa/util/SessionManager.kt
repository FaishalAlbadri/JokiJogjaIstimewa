package com.fanonymous.jogjaistimewa.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.fanonymous.jogjaistimewa.ui.LoginActivity
import java.util.HashMap


class SessionManager {

    private var context: Context

    private val key_id_user = "key_id_user"
    private val is_login = "islogin"
    private val pref_id_user = "pref_id_user"

    private var mode: Int
    private var id_userPref: SharedPreferences
    private var id_userEditor: SharedPreferences.Editor

    constructor(context: Context) {
        mode = 0
        id_userPref = context.getSharedPreferences(pref_id_user, mode)
        id_userEditor = id_userPref.edit()
        this.context = context
    }

    val user: HashMap<String, String?>
        get() {
            val user = HashMap<String, String?>()
            user[key_id_user] = id_userPref.getString(key_id_user, null)
            return user
        }

    fun getIdUser(): String? {
        return id_userPref.getString(key_id_user, null)
    }

    fun createUser(id_user: String?) {
        id_userEditor.putBoolean(is_login, true)
        id_userEditor.putString(key_id_user, id_user)
        id_userEditor.commit()
    }

    fun logout() {
        this.clear()
        val i = Intent(context, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
        (context as Activity).finish()
    }

    fun clear() {
        id_userEditor.clear()
        id_userEditor.commit()
    }
}