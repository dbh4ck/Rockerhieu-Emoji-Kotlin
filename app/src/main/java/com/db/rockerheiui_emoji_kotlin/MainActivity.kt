package com.db.rockerheiui_emoji_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.db.rockerheiui_emoji_kotlin.emoji.Emojicon
import com.db.rockerheiui_emoji_kotlin.helper.EmojiconEditText
import com.db.rockerheiui_emoji_kotlin.helper.EmojiconGridFragment
import com.db.rockerheiui_emoji_kotlin.helper.EmojiconTextView
import com.db.rockerheiui_emoji_kotlin.helper.EmojiconsFragment

class MainActivity : AppCompatActivity(), EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener {
    var RoomMsg: EmojiconEditText? = null
    var RoomTxt: EmojiconTextView? = null

    override fun onEmojiconClicked(emojicon: Emojicon) {
        EmojiconsFragment.input(RoomMsg, emojicon)
        RoomTxt!!.setText(RoomMsg!!.text)
    }

    override fun onEmojiconBackspaceClicked(v: View) {
        EmojiconsFragment.backspace(this!!.RoomMsg!!)
        RoomTxt!!.setText(RoomMsg!!.text)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RoomMsg = findViewById<View>(R.id.RMsgTxt) as EmojiconEditText
        RoomTxt = findViewById<View>(R.id.txtk) as EmojiconTextView
    }
}
