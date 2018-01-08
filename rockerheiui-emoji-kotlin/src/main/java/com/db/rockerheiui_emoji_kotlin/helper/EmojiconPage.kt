package com.db.rockerheiui_emoji_kotlin.helper

import android.support.annotation.DrawableRes
import com.db.rockerheiui_emoji_kotlin.emoji.Emojicon

/**
 * Created by DB on 08-01-2018.
 */
class EmojiconPage {
    @Emojicon.Type
    private var type: Int = 0
    private var data: Array<Emojicon>? = null
    private var useSystemDefaults: Boolean = false
    @DrawableRes
    private var icon: Int = 0

    constructor(@Emojicon.Type type: Int, data: Array<Emojicon>?, useSystemDefaults: Boolean, icon: Int){
        this.type = type;
        this.data = data;
        this.useSystemDefaults = useSystemDefaults;
        this.icon = icon;
    }

    @Emojicon.Type
    fun getType(): Int {
        return type
    }

    fun isUseSystemDefaults(): Boolean {
        return useSystemDefaults
    }

    fun getData(): Array<Emojicon> {
        return this!!.data!!
    }

    fun getIcon(): Int {
        return icon
    }
}