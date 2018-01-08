package com.db.rockerheiui_emoji_kotlin.emoji

import android.os.Parcelable
import android.support.annotation.IntDef
import android.text.style.DynamicDrawableSpan
import java.lang.annotation.RetentionPolicy
import android.os.Parcel

/**
 * Created by DB on 07-01-2018.
 */
class Emojicon : Parcelable {

    @IntDef(DynamicDrawableSpan.ALIGN_BASELINE.toLong(), DynamicDrawableSpan.ALIGN_BOTTOM.toLong())
    annotation class Alignment

    @IntDef(TYPE_UNDEFINED.toLong(), TYPE_PEOPLE.toLong(), TYPE_NATURE.toLong(), TYPE_OBJECTS.toLong(), TYPE_PLACES.toLong(), TYPE_SYMBOLS.toLong())
    @Retention(value = AnnotationRetention.SOURCE)

    annotation class Type

    companion object {
        const val TYPE_UNDEFINED = 0
        const val TYPE_PEOPLE = 1
        const val TYPE_NATURE = 2
        const val TYPE_OBJECTS = 3
        const val TYPE_PLACES = 4
        const val TYPE_SYMBOLS = 5

        fun getEmojicons(@Type type: Int): Array<Emojicon> {
            when (type) {
                TYPE_PEOPLE -> return People.DATA
                TYPE_NATURE -> return Nature.DATA
                TYPE_OBJECTS -> return Objects.DATA
                TYPE_PLACES -> return Places.DATA
                TYPE_SYMBOLS -> return Symbols.DATA
            }
            throw IllegalArgumentException("Invalid emojicon type: " + type)
        }


        val CREATOR: Parcelable.Creator<Emojicon> = object : Parcelable.Creator<Emojicon> {
            override fun createFromParcel(`in`: Parcel): Emojicon {
                return Emojicon(`in`)
            }

            override fun newArray(size: Int): Array<Emojicon?> {
                return arrayOfNulls(size)
            }
        }


        fun fromResource(icon: Int, value: Int): Emojicon {
            val emoji = Emojicon()
            emoji.icon = icon
            emoji.value = value.toChar()
            return emoji
        }

        fun fromCodePoint(codePoint: Int): Emojicon {
            val emoji = Emojicon()
            emoji.emoji = newString(codePoint)
            return emoji
        }

        fun fromChar(ch: Char): Emojicon {
            val emoji = Emojicon()
            emoji.emoji = Character.toString(ch)
            return emoji
        }

        fun fromChars(chars: String): Emojicon {
            val emoji = Emojicon()
            emoji.emoji = chars
            return emoji
        }

        fun newString(codePoint: Int): String {
            return if (Character.charCount(codePoint) == 1) {
                codePoint.toString()
            } else {
                String(Character.toChars(codePoint))
            }
        }

    }



    private var icon: Int = 0

    private var value: Char = ' '

    private var emoji: String? = null

    fun Emojicon(icon: Int, value: Char, emoji: String) {
        this.icon = icon
        this.value = value
        this.emoji = emoji
    }

    constructor(`in`: Parcel) {
        this.icon = `in`.readInt()
        this.value = `in`.readInt().toChar()
        this.emoji = `in`.readString()
    }

    constructor()

    constructor(emoji: String){
        this.emoji = emoji
    }



    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(icon)
        dest.writeInt(value.toInt())
        dest.writeString(emoji)
    }

    fun getValue(): Char {
        return value
    }

    fun getIcon(): Int {
        return icon
    }

    fun getEmoji(): String? {
        return emoji
    }

    override fun equals(o: Any?): Boolean {
        return o is Emojicon && emoji == o.emoji
    }

    override fun hashCode(): Int {
        return emoji!!.hashCode()
    }

}

