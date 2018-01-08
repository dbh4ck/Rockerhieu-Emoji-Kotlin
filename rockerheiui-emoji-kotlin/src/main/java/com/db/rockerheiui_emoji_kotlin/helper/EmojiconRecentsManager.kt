package com.db.rockerheiui_emoji_kotlin.helper

import android.content.Context
import com.db.rockerheiui_emoji_kotlin.emoji.Emojicon
import android.R.id.edit
import android.content.SharedPreferences
import java.util.*

/**
 * Created by DB on 08-01-2018.
 */
class EmojiconRecentsManager: ArrayList<Emojicon> {

    private var mContext: Context? = null

    constructor(context: Context){
        mContext = context.getApplicationContext();
        loadRecents();
    }

    fun getRecentPage(): Int {
        return getPreferences().getInt(PREF_PAGE, 0)
    }

    fun setRecentPage(page: Int) {
        getPreferences().edit().putInt(PREF_PAGE, page).commit()
    }

    fun push(`object`: Emojicon) {
        // FIXME totally inefficient way of adding the emoji to the adapter
        // TODO this should be probably replaced by a deque
        if (contains(`object`)) {
            super.remove(`object`)
        }
        add(0, `object`)
    }

    override fun add(`object`: Emojicon): Boolean {
        val ret = super.add(`object`)

        while (this.size > EmojiconRecentsManager.maximumSize!!) {
            super.removeAt(0)
        }

        saveRecents()
        return ret
    }


    override fun add(index: Int, `object`: Emojicon) {
        super.add(index, `object`)

        if (index == 0) {
            while (this.size > EmojiconRecentsManager.maximumSize!!) {
                super.remove(EmojiconRecentsManager.maximumSize as Emojicon)
            }
        } else {
            while (this.size > EmojiconRecentsManager.maximumSize!!) {
                super.removeAt(0)
            }
        }

        saveRecents()
    }


    override fun remove(`object`: Emojicon): Boolean {
        val ret = super.remove(`object`)
        saveRecents()
        return ret
    }

    private fun getPreferences(): SharedPreferences {
        return mContext!!.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private fun loadRecents() {
        val prefs = getPreferences()
        val str = prefs.getString(PREF_RECENTS, "")
        val tokenizer = StringTokenizer(str, DELIMITER)
        while (tokenizer.hasMoreTokens()) {
            add(Emojicon.fromChars(tokenizer.nextToken()))
        }
    }

    private fun saveRecents() {
        val str = StringBuilder()
        val c = size
        for (i in 0 until c) {
            val e = get(i)
            str.append(e.getEmoji())
            if (i < c - 1) {
                str.append(EmojiconRecentsManager.DELIMITER)
            }
        }
        val prefs = getPreferences()
        prefs.edit().putString(PREF_RECENTS, str.toString()).commit()
    }

    companion object {
        private var DELIMITER: String = ","
        private var PREFERENCE_NAME: String = "emojicon"
        private var PREF_RECENTS: String = "recent_emojis"
        private var PREF_PAGE: String = "recent_page"

        private var LOCK = Any()
        private var sInstance: EmojiconRecentsManager? = null
        private var maximumSize: Int? = 40

        fun setMaximumSize(maximumSize: Int) {
            EmojiconRecentsManager.maximumSize = maximumSize
        }

        fun getInstance(context: Context): EmojiconRecentsManager {
            if (sInstance == null) {
                synchronized(LOCK) {
                    if (sInstance == null) {
                        sInstance = EmojiconRecentsManager(context)
                    }
                }
            }
            return this!!.sInstance!!
        }
    }

}