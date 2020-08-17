package android.com.moviebuff.ui

import android.com.moviebuff.R
import android.os.Bundle
import android.util.SparseArray
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }
    private fun initUi() {
        pagerAdapter = ViewPagerAdapter()
        viewPager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewPager)
    }




    inner class ViewPagerAdapter : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragments: SparseArray<Fragment> = SparseArray()

        override fun getItem(position: Int): Fragment {
            return MovieListFragment.newInstance("now_playing")
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment: Fragment = super.instantiateItem(container, position) as Fragment
            fragments.put(position, fragment)
            return fragment
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
            fragments.remove(position)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return "Now Playing"
        }

        override fun getCount(): Int {
            return 1
        }
    }
}
