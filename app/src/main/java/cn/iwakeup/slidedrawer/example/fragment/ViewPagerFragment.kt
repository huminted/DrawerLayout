package cn.iwakeup.slidedrawer.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import cn.iwakeup.slidedrawer.DrawerLayout
import cn.iwakeup.slidedrawer.example.R
import cn.iwakeup.slidedrawer.example.list.VerticalAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerFragment : Fragment(R.layout.fragment_vp) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        viewPager.adapter = PagerAdapter(childFragmentManager, this.lifecycle).apply {
            addFragment(NestedListFragment.newInstance(1))
            addFragment(PageFragment.newInstance(2))
            addFragment(PageFragment.newInstance(3))
            addFragment(PageFragment.newInstance(4))
        }
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "TAB ${(position + 1)}"
        }.attach()


        ViewCompat.setOnApplyWindowInsetsListener(tabLayout) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }
         
            WindowInsetsCompat.CONSUMED
        }

        val window = requireActivity().window

        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true
    }


}