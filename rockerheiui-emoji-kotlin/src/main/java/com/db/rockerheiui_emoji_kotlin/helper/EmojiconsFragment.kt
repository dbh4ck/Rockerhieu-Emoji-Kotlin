package com.db.rockerheiui_emoji_kotlin.helper

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v4.view.PagerAdapter
import android.view.View
import android.os.Bundle
import android.os.Handler
import com.db.rockerheiui_emoji_kotlin.emoji.Emojicon
import android.view.ViewGroup
import android.view.LayoutInflater
import com.db.rockerheiui_emoji_kotlin.R
import java.util.*
import android.text.Selection.getSelectionEnd
import android.text.Selection.getSelectionStart
import android.view.KeyEvent
import android.widget.EditText
import android.view.KeyEvent.KEYCODE_ENDCALL
import android.view.KeyEvent.KEYCODE_DEL
import android.widget.LinearLayout
import java.nio.file.Files.size
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.MotionEvent
import android.os.SystemClock
import android.support.v4.app.FragmentManager


/**
 * Created by DB on 08-01-2018.
 */
class EmojiconsFragment: Fragment(), ViewPager.OnPageChangeListener, EmojiconRecents {
    private var mOnEmojiconBackspaceClickedListener: OnEmojiconBackspaceClickedListener? = null
    private var mEmojiTabLastSelectedIndex = -1
    private var mEmojiTabs: Array<View?>? = null
    private var mViewPager: ViewPager? = null
    private var mEmojisAdapter: PagerAdapter? = null
    private var mRecentsManager: EmojiconRecentsManager? = null
    private var mUseSystemDefault = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.emojicons, container, false)
        mViewPager = view.findViewById(R.id.emojis_pager)
        @Suppress("DEPRECATION")
        mViewPager!!.setOnPageChangeListener(this)

        val recents = this
        mEmojisAdapter = EmojiconGridFragmentPagerAdapter(fragmentManager, Arrays.asList(
                EmojiconRecentsGridFragment.newInstance(mUseSystemDefault),
                EmojiconGridFragment.newInstance(Emojicon.TYPE_PEOPLE, recents, mUseSystemDefault),
                EmojiconGridFragment.newInstance(Emojicon.TYPE_NATURE, recents, mUseSystemDefault),
                EmojiconGridFragment.newInstance(Emojicon.TYPE_OBJECTS, recents, mUseSystemDefault),
                EmojiconGridFragment.newInstance(Emojicon.TYPE_PLACES, recents, mUseSystemDefault),
                EmojiconGridFragment.newInstance(Emojicon.TYPE_SYMBOLS, recents, mUseSystemDefault)
        ))
        mViewPager!!.adapter = mEmojisAdapter

        mEmojiTabs = arrayOfNulls(6)
        mEmojiTabs!![0] = view.findViewById(R.id.emojis_tab_0_recents)
        mEmojiTabs!![1] = view.findViewById(R.id.emojis_tab_1_people)
        mEmojiTabs!![2] = view.findViewById(R.id.emojis_tab_2_nature)
        mEmojiTabs!![3] = view.findViewById(R.id.emojis_tab_3_objects)
        mEmojiTabs!![4] = view.findViewById(R.id.emojis_tab_4_cars)
        mEmojiTabs!![5] = view.findViewById(R.id.emojis_tab_5_punctuation)
        for (i in 0 until mEmojiTabs!!.size) {
            mEmojiTabs!![i]!!.setOnClickListener { mViewPager!!.currentItem = i }
        }
        view.findViewById<View>(R.id.emojis_backspace).setOnTouchListener(RepeatListener(1000, 50, View.OnClickListener { v -> mOnEmojiconBackspaceClickedListener?.onEmojiconBackspaceClicked(v) }))

        // get last selected page
        mRecentsManager = EmojiconRecentsManager.getInstance(view.getContext())
        var page = mRecentsManager!!.getRecentPage()
        // last page was recents, check if there are recents to use
        // if none was found, go to page 1
        if (page == 0 && mRecentsManager!!.size === 0) {
            page = 1
        }

        if (page == 0) {
            onPageSelected(page)
        } else {
            mViewPager!!.setCurrentItem(page, false)
        }
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is OnEmojiconBackspaceClickedListener) {
            mOnEmojiconBackspaceClickedListener = activity as OnEmojiconBackspaceClickedListener
        } else if (parentFragment is OnEmojiconBackspaceClickedListener) {
            mOnEmojiconBackspaceClickedListener = parentFragment as OnEmojiconBackspaceClickedListener
        } else {
            throw IllegalArgumentException(context.toString() + " must implement interface " + OnEmojiconBackspaceClickedListener::class.java.simpleName)
        }
    }

    override fun onDetach() {
        mOnEmojiconBackspaceClickedListener = null
        super.onDetach()
    }

    override fun addRecentEmoji(context: Context, emojicon: Emojicon) {
        val fragment = mEmojisAdapter!!.instantiateItem(mViewPager, 0) as EmojiconRecentsGridFragment
        fragment.addRecentEmoji(context, emojicon)
    }


    override fun onPageScrolled(i: Int, v: Float, i2: Int) {}

    override fun onPageSelected(i: Int) {
        if (mEmojiTabLastSelectedIndex === i) {
            return
        }
        when (i) {
            0, 1, 2, 3, 4, 5 -> {
                if (mEmojiTabLastSelectedIndex >= 0 && mEmojiTabLastSelectedIndex < mEmojiTabs!!.size) {
                    mEmojiTabs!![mEmojiTabLastSelectedIndex]!!.isSelected = false
                }
                mEmojiTabs!![i]!!.isSelected = true
                mEmojiTabLastSelectedIndex = i
                mRecentsManager!!.setRecentPage(i)
            }
        }
        setSelectedTabIndicator(this.mEmojiTabLastSelectedIndex)
    }

    private fun setSelectedTabIndicator(selectedTabIndex: Int) {
        for (x in 0 until this.mEmojiTabs!!.size) {
            val v = this.mEmojiTabs!![x]
            if (x == selectedTabIndex) {
                (v!!.parent as LinearLayout).getChildAt(0).setBackgroundResource(R.color.orange)
            } else {
                (v!!.parent as LinearLayout).getChildAt(0).setBackgroundResource(R.color.emojicon_normal)
            }
        }
    }

    override fun onPageScrollStateChanged(i: Int) {}

    interface OnEmojiconBackspaceClickedListener {
        fun onEmojiconBackspaceClicked(v: View)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mUseSystemDefault = arguments.getBoolean(USE_SYSTEM_DEFAULT_KEY)
        } else {
            mUseSystemDefault = false
        }
    }

    companion object {
        private val USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults"

        fun newInstance(useSystemDefault: Boolean): EmojiconsFragment {
            val fragment = EmojiconsFragment()
            val bundle = Bundle()
            bundle.putBoolean(USE_SYSTEM_DEFAULT_KEY, useSystemDefault)
            fragment.arguments = bundle
            return fragment
        }

        fun input(editText: EditText?, emojicon: Emojicon?) {
            if (editText == null || emojicon == null) {
                return
            }

            val start = editText.selectionStart
            val end = editText.selectionEnd
            if (start < 0) {
                editText.append(emojicon.getEmoji())
            } else {
                editText.text.replace(Math.min(start, end), Math.max(start, end), emojicon.getEmoji(), 0, emojicon.getEmoji()!!.length)
            }
        }

        fun backspace(editText: EditText) {
            val event = KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL)
            editText.dispatchKeyEvent(event)
        }

        private class EmojiconGridFragmentPagerAdapter(fm: FragmentManager, private val fragments: List<EmojiconGridFragment>) : FragmentStatePagerAdapter(fm) {

            override fun getItem(i: Int): Fragment {
                return fragments[i]
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }

        class RepeatListener(private val initialInterval: Int, private val normalInterval: Int, private val clickListener: View.OnClickListener?) : View.OnTouchListener {

            private val handler = Handler()

            private val handlerRunnable = object : Runnable {
                override fun run() {
                    if (downView == null) {
                        return
                    }
                    handler.removeCallbacksAndMessages(downView)
                    handler.postAtTime(this, downView, SystemClock.uptimeMillis() + normalInterval)
                    clickListener!!.onClick(downView)
                }
            }

            private var downView: View? = null


            init {
                if (clickListener == null)
                    throw IllegalArgumentException("null runnable")
                if (initialInterval < 0 || normalInterval < 0)
                    throw IllegalArgumentException("negative interval")
            }

            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downView = view
                        handler.removeCallbacks(handlerRunnable)
                        handler.postAtTime(handlerRunnable, downView, SystemClock.uptimeMillis() + initialInterval)
                        clickListener!!.onClick(view)
                        return true
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                        handler.removeCallbacksAndMessages(downView)
                        downView = null
                        return true
                    }
                }
                return false
            }
        }
    }
}