package com.db.rockerheiui_emoji_kotlin.helper

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import com.db.rockerheiui_emoji_kotlin.R
import com.db.rockerheiui_emoji_kotlin.emoji.Emojicon
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by DB on 08-01-2018.
 */
class EmojiconAdapter: ArrayAdapter<Emojicon> {
    private var mUseSystemDefault = false

    constructor(context: Context, data: List<Emojicon>): super(context, R.layout.emojicon_item, data){
        mUseSystemDefault = false;
    }

    constructor(context: Context, data: List<Emojicon>, useSystemDefault: Boolean): super(context, R.layout.emojicon_item, data){
        mUseSystemDefault = useSystemDefault
    }


    constructor(context: Context, data: Array<Emojicon>): super(context, R.layout.emojicon_item, data){
        mUseSystemDefault = false
    }

    constructor(context: Context, data: Array<Emojicon>, useSystemDefault: Boolean): super(context, R.layout.emojicon_item, data){
        mUseSystemDefault = useSystemDefault
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: View?
        val vh: ViewHolder

        if (convertView == null) {
            v = View.inflate(context, R.layout.emojicon_item, null)
            vh = ViewHolder(v)
            v?.setTag(vh)
        }else{
            v = convertView
            vh = v.tag as ViewHolder
        }

        val emoji = getItem(position)
        vh.icon!!.setText(emoji!!.getEmoji())
        vh.icon!!.setUseSystemDefault(mUseSystemDefault)

        return v!!
    }

    private class ViewHolder(row : View?){
        var icon : EmojiconTextView? = null

        init {
            this.icon = row?.findViewById<View>(R.id.emojicon_icon) as EmojiconTextView

        }
    }
}