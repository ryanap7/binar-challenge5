package id.ryan.suitgame

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ScreenSlideAdapter(activity: AppCompatActivity, private val itemsCount: Int) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return ScreenSlideFragment.getInstance(position)
    }
}