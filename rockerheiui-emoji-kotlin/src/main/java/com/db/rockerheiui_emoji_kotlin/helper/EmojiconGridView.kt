package com.db.rockerheiui_emoji_kotlin.helper

import android.content.Context
import android.widget.AdapterView
import android.widget.GridView
import com.db.rockerheiui_emoji_kotlin.emoji.Emojicon
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import com.db.rockerheiui_emoji_kotlin.R
import java.util.*


/**
 * Created by DB on 08-01-2018.
 */
class EmojiconGridView: GridView, AdapterView.OnItemClickListener {
    @Emojicon.Type
    private var mType: Int = 0
    private var mData: Array<Emojicon>? = null
    private var mUseSystemDefaults: Boolean = false
    private var onEmojiconClickedListener: EmojiconGridFragment.OnEmojiconClickedListener? = null
    private var mEmojiAdapter: EmojiconAdapter? = null
    private var mEmojiList = ArrayList<Emojicon>()


    constructor(context: Context) : super(context, null){

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.attr.emojiconGridViewStyle){
        onItemClickListener = this
        isSaveEnabled = true
        mEmojiList = ArrayList()
        mEmojiAdapter = EmojiconAdapter(context, mEmojiList)
        adapter = mEmojiAdapter
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.data = mData
        ss.type = mType
        ss.useSystemDefaults = mUseSystemDefaults
        ss.scrollX = scrollX
        ss.scrollY = scrollY
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superState)
        mType = state.type
        mData = state.data
        mUseSystemDefaults = state.useSystemDefaults
        scrollX = state.scrollX
        scrollY = state.scrollY
        setEmojiData(mType, this!!.mData!!, mUseSystemDefaults)
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    fun setEmojiData(@Emojicon.Type type: Int, data: Array<Emojicon>, useSystemDefaults: Boolean) {
        this.mType = type
        if (this.mType !== Emojicon.TYPE_UNDEFINED) {
            this.mData = Emojicon.getEmojicons(type)
        } else {
            this.mData = data
        }
        this.mUseSystemDefaults = useSystemDefaults
        if (this.mData == null) {
            mEmojiList.clear()
        } else {
            mEmojiList.clear()
            Collections.addAll<Emojicon>(mEmojiList, *this.mData!!)
        }
        mEmojiAdapter!!.notifyDataSetChanged()
    }

    fun setOnEmojiconClickedListener(onEmojiconClickedListener: EmojiconGridFragment.OnEmojiconClickedListener) {
        this.onEmojiconClickedListener = onEmojiconClickedListener
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        onEmojiconClickedListener?.onEmojiconClicked(parent.getItemAtPosition(position) as Emojicon)
    }


    companion object {

        class SavedState : View.BaseSavedState {
            @Emojicon.Type
            internal var type: Int = 0
            internal var data: Array<Emojicon>? = null
            internal var useSystemDefaults: Boolean = false
            internal var scrollX: Int = 0
            internal var scrollY: Int = 0

            internal constructor(superState: Parcelable) : super(superState) {}

            private constructor(`in`: Parcel) : super(`in`) {

                this.type = `in`.readInt()
                this.data = `in`.readParcelableArray(Emojicon::class.java.classLoader) as Array<Emojicon>
                this.useSystemDefaults = `in`.readInt() != 0
                this.scrollX = `in`.readInt()
                this.scrollY = `in`.readInt()
            }

            override fun writeToParcel(out: Parcel, flags: Int) {
                super.writeToParcel(out, flags)
                out.writeInt(this.type)
                out.writeParcelableArray(data, flags)
                out.writeInt(if (this.useSystemDefaults) 1 else 0)
                out.writeInt(this.scrollX)
                out.writeInt(this.scrollY)
            }


            //required field that makes Parcelables from a Parcel
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }

        }
    }

}