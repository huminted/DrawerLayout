package cn.iwakeup.slidedrawer.example.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentList = mutableListOf<Fragment>()

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size - 1)
    }

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }


}