package com.db.rockerheiui_emoji_kotlin.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.DynamicDrawableSpan
import android.graphics.drawable.Drawable
import java.lang.ref.WeakReference


/**
 * Created by DB on 08-01-2018.
 */
class EmojiconSpan: DynamicDrawableSpan {

    private var mContext: Context? = null

    private var mResourceId: Int = 0

    private var mSize: Int = 0

    private var mTextSize: Int = 0

    private var mHeight: Int = 0

    private var mWidth: Int = 0

    private var mTop: Int = 0

    private var mDrawable: Drawable? = null

    private var mDrawableRef: WeakReference<Drawable>? = null

    constructor(context: Context, resourceId: Int, size: Int, alignment: Int, textSize: Int): super(alignment){

        mContext = context
        mResourceId = resourceId
        mWidth = size
        mHeight = size
        mSize = size
        mTextSize = textSize
    }

    override fun getDrawable(): Drawable {
        if (mDrawable == null) {
            try {
                mDrawable = mContext!!.getResources().getDrawable(mResourceId)
                mHeight = mSize
                mWidth = mHeight * mDrawable!!.getIntrinsicWidth() / mDrawable!!.getIntrinsicHeight()
                mTop = (mTextSize - mHeight) / 2
                mDrawable!!.setBounds(0, mTop, mWidth, mTop + mHeight)
            } catch (e: Exception) {
                // swallow
            }

        }
        return mDrawable!!
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        //super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        val b = getCachedDrawable()
        canvas.save()

        var transY = bottom - b.bounds.bottom
        if (mVerticalAlignment == DynamicDrawableSpan.ALIGN_BASELINE) {
            transY = top + (bottom - top) / 2 - (b.bounds.bottom - b.bounds.top) / 2 - mTop
        }

        canvas.translate(x, transY.toFloat())
        b.draw(canvas)
        canvas.restore()
    }

    private fun getCachedDrawable(): Drawable {
        if (mDrawableRef == null || mDrawableRef!!.get() == null) {
            mDrawableRef = WeakReference(drawable)
        }
        return mDrawableRef!!.get()!!
    }
}