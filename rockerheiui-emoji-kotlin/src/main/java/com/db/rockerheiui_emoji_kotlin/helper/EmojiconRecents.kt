package com.db.rockerheiui_emoji_kotlin.helper

import android.content.Context
import com.db.rockerheiui_emoji_kotlin.emoji.Emojicon


/**
 * Created by DB on 08-01-2018.
 */
interface EmojiconRecents {
    fun addRecentEmoji(context: Context, emojicon: Emojicon)
}