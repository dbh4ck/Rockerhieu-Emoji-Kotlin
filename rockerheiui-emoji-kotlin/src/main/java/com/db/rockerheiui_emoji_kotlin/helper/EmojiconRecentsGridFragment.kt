package com.db.rockerheiui_emoji_kotlin.helper

import android.content.Context
import com.db.rockerheiui_emoji_kotlin.emoji.Emojicon
import android.widget.GridView
import android.os.Bundle
import android.view.View
import com.db.rockerheiui_emoji_kotlin.R


/**
 * Created by DB on 08-01-2018.
 */
class EmojiconRecentsGridFragment: EmojiconGridFragment(), EmojiconRecents {
    private var mAdapter: EmojiconAdapter? = null
    private var mUseSystemDefault = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mUseSystemDefault = arguments.getBoolean(USE_SYSTEM_DEFAULT_KEY)
        } else {
            mUseSystemDefault = false
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val recents = EmojiconRecentsManager
                .getInstance(view!!.getContext())

        mAdapter = EmojiconAdapter(view.getContext(), recents, mUseSystemDefault)
        val gridView = view.findViewById(R.id.Emoji_GridView) as GridView
        gridView.adapter = mAdapter
        gridView.onItemClickListener = this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAdapter = null
    }

    override fun addRecentEmoji(context: Context, emojicon: Emojicon) {
        val recents = EmojiconRecentsManager
                .getInstance(context)
        recents.push(emojicon)

        // notify dataset changed
        mAdapter?.notifyDataSetChanged()
    }


    companion object {
        private val USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults"

        protected fun newInstance(): EmojiconRecentsGridFragment {
            return newInstance(false)
        }

        fun newInstance(useSystemDefault: Boolean): EmojiconRecentsGridFragment {
            val fragment = EmojiconRecentsGridFragment()
            val bundle = Bundle()
            bundle.putBoolean(USE_SYSTEM_DEFAULT_KEY, useSystemDefault)
            fragment.arguments = bundle
            return fragment
        }
    }
}