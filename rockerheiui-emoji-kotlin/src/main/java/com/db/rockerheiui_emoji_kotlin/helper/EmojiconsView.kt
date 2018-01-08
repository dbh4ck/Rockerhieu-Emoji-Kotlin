package com.db.rockerheiui_emoji_kotlin.helper

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.view.ViewGroup
import com.db.rockerheiui_emoji_kotlin.R
import android.os.Build
import android.support.annotation.RequiresApi
import java.util.*
import android.widget.LinearLayout
import android.widget.ImageButton
import android.widget.ImageView
import java.nio.file.Files.size
import android.net.http.SslCertificate.restoreState
import android.os.Parcelable
import android.os.Bundle
import android.util.SparseArray
import android.view.View.generateViewId
import android.support.annotation.NonNull
import android.support.v4.view.PagerAdapter
import com.db.rockerheiui_emoji_kotlin.util.Utils

/**
 * Created by DB on 08-01-2018.
 */
class EmojiconsView: FrameLayout, ViewPager.OnPageChangeListener {
    private var mViewPager: ViewPager? = null
    private var mPages: List<EmojiconPage>? = null
    private var mTabsContainer: ViewGroup? = null
    private var mTabs: Array<View?>? = null
    private var mLastTab: View? = null

    constructor(context: Context): this(context, null){
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs){
        LayoutInflater.from(context).inflate(R.layout.emojicons_view, this);
        mViewPager = findViewById<View>(R.id.emojis_pager) as ViewPager
        mTabsContainer = findViewById<View>(R.id.emojis_tab) as ViewGroup
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mViewPager!!.addOnPageChangeListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mViewPager!!.removeOnPageChangeListener(this)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun setPages(pages: List<EmojiconPage>) {
        this.mPages = pages
        if (mTabs == null || mTabs!!.size !== pages.size) {
            mTabs = arrayOfNulls(pages.size)
        } else {
            Arrays.fill(mTabs, null)
        }

        for (i in 0 until mTabsContainer!!.getChildCount() - 2) {
            mTabsContainer!!.removeViewAt(0)
        }

        var index = 0
        for (page in pages) {
            addTabIcon(page, index++)
            addTabDivider()
        }
        onPageSelected(0)
        mViewPager!!.setAdapter(EmojiconGridViewPagerAdapter(context, pages))
    }

    private fun addTabDivider() {
        val divider = View(context)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            @Suppress("DEPRECATION")
            mViewPager!!.setBackgroundColor(context.resources.getColor(R.color.horizontal_vertical))
        } else {
            mViewPager!!.setBackgroundColor(context.getColor(R.color.horizontal_vertical))
        }
        val params = LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT)
        mTabsContainer!!.addView(divider, mTabsContainer!!.getChildCount() - 2, params)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun addTabIcon(page: EmojiconPage, index: Int) {
        val icon = ImageButton(context)
        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT)
        params.weight = 1f
        icon.background = null
        icon.scaleType = ImageView.ScaleType.CENTER
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            icon.setImageDrawable(context.resources.getDrawable(page.getIcon()))
        } else {
            icon.setImageDrawable(context.getDrawable(page.getIcon()))
        }
        mTabsContainer!!.addView(icon, mTabsContainer!!.getChildCount() - 2, params)
        mTabs!![index] = icon
        icon.setOnClickListener { mViewPager!!.setCurrentItem(index, true) }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        if (mTabs == null || position >= mTabs!!.size) {
            return
        }
        if (mLastTab != null) {
            mLastTab!!.setSelected(false)
        }
        mLastTab = mTabs!![position]
        mLastTab!!.setSelected(true)
    }

    override fun onPageScrollStateChanged(state: Int) {}

    companion object {
        internal class EmojiconGridViewPagerAdapter(private val context: Context, private val pages: List<EmojiconPage>) : PagerAdapter() {
            private var savedStates: Array<EmojiconGridView.Companion.SavedState?>? = null

            init {
                this.savedStates = arrayOfNulls<EmojiconGridView.Companion.SavedState>(pages.size)
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val emojiconPage = pages[position]
                val emojiGridView = EmojiconGridView(context)
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    emojiGridView.setId(View.generateViewId())
                } else {
                    emojiGridView.setId(Utils.generateViewId())
                }
                container.addView(emojiGridView)
                emojiGridView.setEmojiData(emojiconPage.getType(), emojiconPage.getData(), emojiconPage.isUseSystemDefaults())
                if (savedStates!![position] != null) {
                    val sparseArray = SparseArray<Parcelable>(1)
                    sparseArray.put(emojiGridView.getId(), savedStates!![position])
                    emojiGridView.restoreHierarchyState(sparseArray)
                }
                return emojiGridView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                val view = `object` as EmojiconGridView
                savedStates!![position] = view.onSaveInstanceState() as EmojiconGridView.Companion.SavedState
                container.removeView(`object` as View)
            }

            override fun saveState(): Parcelable {
                val state = Bundle()
                state.putParcelableArray("states", savedStates)
                return state
            }

            override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
                if (state != null) {
                    val bundle = state as Bundle?
                    val states = bundle!!.getParcelableArray("states")
                    savedStates = arrayOfNulls<EmojiconGridView.Companion.SavedState>(states!!.size)
                    for (i in states.indices) {
                        savedStates!![i] = states[i] as EmojiconGridView.Companion.SavedState
                    }
                }
                super.restoreState(state, loader)
            }

            override fun getCount(): Int {
                return pages.size
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }
        }
    }
}