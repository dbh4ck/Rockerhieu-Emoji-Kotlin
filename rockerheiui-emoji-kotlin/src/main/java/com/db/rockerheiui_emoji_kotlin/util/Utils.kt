package com.db.rockerheiui_emoji_kotlin.util

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.os.Build
import android.support.annotation.RequiresApi
import java.util.concurrent.atomic.AtomicInteger
import android.content.res.Configuration

/**
 * Created by DB on 08-01-2018.
 */
class Utils {
    companion object {
        private var sKeyboardHeight: Int = 0


        private val sNextGeneratedId = AtomicInteger(1)

        /**
         * Generate a value suitable for use in [View.setId].
         * This value will not collide with ID values generated at build time by aapt for R.id.
         *
         * @return a generated ID value
         */
        fun generateViewId(): Int {
            while (true) {
                val result = sNextGeneratedId.get()
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                var newValue = result + 1
                if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result
                }
            }
        }

        fun getSoftKeyboardHeight(context: Context): Int {
            return 200
        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
        fun getScreenHeight(context: Context): Int {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.y
        }

        fun getScreenWidth(context: Context): Int {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.x
        }

        fun isKeyboardShowing(context: Context): Boolean {
            return context.getResources().getConfiguration().keyboardHidden === Configuration.KEYBOARDHIDDEN_NO
        }

        fun setKeyboardHeight(keyboardHeight: Int) {
            Utils.sKeyboardHeight = keyboardHeight
        }

        fun getKeyboardHeight(): Int {
            return sKeyboardHeight
        }
    }
}