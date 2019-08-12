package com.github.watabee.flowsample

import android.content.res.Resources
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = Adapter(supportFragmentManager, resources)

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)
    }

    private class Adapter(
        fragmentManager: FragmentManager,
        private val resources: Resources
    ) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = when (Page.valueOf(position)) {
            Page.RANKING -> RankingItemsFragment.newInstance()
            Page.FAVORITE -> FavoriteItemsFragment.newInstance()
        }

        override fun getCount(): Int = Page.values().size

        override fun getPageTitle(position: Int): CharSequence? {
            return resources.getString(Page.valueOf(position).titleResId)
        }
    }

    private enum class Page(@StringRes val titleResId: Int) {
        RANKING(R.string.ranking), FAVORITE(R.string.favorite);

        companion object {
            fun valueOf(index: Int): Page =
                values().getOrNull(index) ?: throw IllegalArgumentException()
        }
    }
}
