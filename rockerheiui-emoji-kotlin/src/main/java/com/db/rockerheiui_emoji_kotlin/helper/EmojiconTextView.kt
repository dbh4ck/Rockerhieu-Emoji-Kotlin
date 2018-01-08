package com.db.rockerheiui_emoji_kotlin.helper

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.DynamicDrawableSpan
import com.db.rockerheiui_emoji_kotlin.emoji.Emojicon
import android.content.res.TypedArray
import android.widget.TextView
import com.db.rockerheiui_emoji_kotlin.R

/**
 * Created by DB on 08-01-2018.
 */
class EmojiconTextView: AppCompatTextView {

    private var mEmojiconSize: Int = 0
    private var mEmojiconAlignment: Int = 0
    private var mEmojiconTextSize: Int = 0
    private var mTextStart = 0
    private var mTextLength = -1
    private var mUseSystemDefault = false


    constructor(context : Context) : super(context){
        init(null)
    }

    constructor(context: Context, attrs : AttributeSet) : super(context, attrs){
        init(attrs);
    }
    constructor(context: Context, attrs: AttributeSet, defStyle : Int) : super(context, attrs, defStyle){
        init(attrs);
    }

    private fun init(attrs: AttributeSet?) {
        mEmojiconTextSize = textSize.toInt()
        if (attrs == null) {
            mEmojiconSize = textSize.toInt()
        } else {
            val a = context.obtainStyledAttributes(attrs, R.styleable.Emojicon)
            mEmojiconSize = a.getDimension(R.styleable.Emojicon_emojiconSize, textSize).toInt()
            mEmojiconAlignment = a.getInt(R.styleable.Emojicon_emojiconAlignment, DynamicDrawableSpan.ALIGN_BASELINE)
            mTextStart = a.getInteger(R.styleable.Emojicon_emojiconTextStart, 0)
            mTextLength = a.getInteger(R.styleable.Emojicon_emojiconTextLength, -1)
            mUseSystemDefault = a.getBoolean(R.styleable.Emojicon_emojiconUseSystemDefault, false)
            a.recycle()
        }
        text = text
    }

    override fun setText(text: CharSequence, type: TextView.BufferType) {
        var text = text
        if (!TextUtils.isEmpty(text)) {
            val builder = SpannableStringBuilder(text)
            EmojiconHandler.addEmojis(context, builder, mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mTextStart, mTextLength, mUseSystemDefault)
            text = builder
        }
        super.setText(text, type)
    }

    /**
     * Set the size of emojicon in pixels.
     */
    fun setEmojiconSize(pixels: Int) {
        mEmojiconSize = pixels
        super.setText(text)
    }

    /**
     * Set whether to use system default emojicon
     */
    fun setUseSystemDefault(useSystemDefault: Boolean) {
        mUseSystemDefault = useSystemDefault
    }

}