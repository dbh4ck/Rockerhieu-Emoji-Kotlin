package com.db.rockerheiui_emoji_kotlin.helper

import android.content.Context
import android.support.v4.app.Fragment
import android.widget.AdapterView
import com.db.rockerheiui_emoji_kotlin.emoji.Emojicon
import android.os.Bundle
import android.os.Parcelable
import android.widget.GridView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.db.rockerheiui_emoji_kotlin.R
import com.db.rockerheiui_emoji_kotlin.emoji.People

/**
 * Created by DB on 08-01-2018.
 */
open class EmojiconGridFragment: Fragment(), AdapterView.OnItemClickListener {
    private var mOnEmojiconClickedListener: OnEmojiconClickedListener? = null
    private var mRecents: EmojiconRecents? = null
    private var mEmojicons: Array<Emojicon>? = null
    @Emojicon.Type
    private var mEmojiconType: Int = 0
    private var mUseSystemDefault = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.emojicon_grid, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val gridView = view!!.findViewById<View>(R.id.Emoji_GridView) as GridView
        val bundle = arguments
        if (bundle == null) {
            mEmojiconType = Emojicon.TYPE_UNDEFINED
            mEmojicons = People.DATA
            mUseSystemDefault = false
        } else {

            mEmojiconType = bundle.getInt(ARG_EMOJICON_TYPE)
            if (mEmojiconType === Emojicon.TYPE_UNDEFINED) {
                val parcels = bundle.getParcelableArray(ARG_EMOJICONS)
                mEmojicons = arrayOf<Emojicon>(parcels.size as Emojicon)
                for (i in parcels.indices) {
                    mEmojicons!![i] = parcels[i] as Emojicon
                }
            } else {
                mEmojicons = Emojicon.getEmojicons(mEmojiconType)
            }
            mUseSystemDefault = bundle.getBoolean(ARG_USE_SYSTEM_DEFAULTS)
        }
        gridView.adapter = EmojiconAdapter(view.context, mEmojicons!!, mUseSystemDefault)
        gridView.onItemClickListener = this
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putParcelableArray(ARG_EMOJICONS, mEmojicons)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEmojiconClickedListener) {
            mOnEmojiconClickedListener = context
        } else if (parentFragment is OnEmojiconClickedListener) {
            mOnEmojiconClickedListener = parentFragment as OnEmojiconClickedListener
        } else {
            throw IllegalArgumentException(context.toString() + " must implement interface " + OnEmojiconClickedListener::class.java.simpleName)
        }
    }

    override fun onDetach() {
        mOnEmojiconClickedListener = null
        super.onDetach()
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        mOnEmojiconClickedListener?.onEmojiconClicked(parent.getItemAtPosition(position) as Emojicon)
        mRecents?.addRecentEmoji(view.context, parent.getItemAtPosition(position) as Emojicon)
    }

    private fun setRecents(recents: EmojiconRecents) {
        mRecents = recents
    }

    interface OnEmojiconClickedListener {
        fun onEmojiconClicked(emojicon: Emojicon)
    }

    companion object {
        private val ARG_USE_SYSTEM_DEFAULTS = "useSystemDefaults"
        private val ARG_EMOJICONS = "emojicons"
        private val ARG_EMOJICON_TYPE = "emojiconType"


        protected fun newInstance(emojicons: Array<Emojicon>, recents: EmojiconRecents): EmojiconGridFragment {
            return newInstance(Emojicon.TYPE_UNDEFINED, emojicons, recents, false)
        }

        fun newInstance(
                @Emojicon.Type type: Int, recents: EmojiconRecents, useSystemDefault: Boolean): EmojiconGridFragment {
            return newInstance(type, null, recents, useSystemDefault)
        }

        protected fun newInstance(
                @Emojicon.Type type: Int, emojicons: Array<Emojicon>?, recents: EmojiconRecents, useSystemDefault: Boolean): EmojiconGridFragment {
            val emojiGridFragment = EmojiconGridFragment()
            val args = Bundle()
            args.putInt(ARG_EMOJICON_TYPE, type)
            args.putParcelableArray(ARG_EMOJICONS, emojicons)
            args.putBoolean(ARG_USE_SYSTEM_DEFAULTS, useSystemDefault)
            emojiGridFragment.arguments = args
            emojiGridFragment.setRecents(recents)
            return emojiGridFragment
        }
    }
}