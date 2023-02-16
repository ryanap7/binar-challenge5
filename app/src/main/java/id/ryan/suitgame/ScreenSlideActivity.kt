package id.ryan.suitgame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager2.widget.ViewPager2
import id.ryan.suitgame.databinding.ActivityScreenSlideBinding

class ScreenSlideActivity : AppCompatActivity() {

    private var onBoardingPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            updateCircleMarker(binding, position)
        }
    }
    private lateinit var binding: ActivityScreenSlideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenSlideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val numberOfScreens = resources.getStringArray(R.array.on_boarding_titles).size
        val onBoardingAdapter = ScreenSlideAdapter(this, numberOfScreens)
        binding.onBoardingViewPager.adapter = onBoardingAdapter
        binding.onBoardingViewPager.registerOnPageChangeCallback(onBoardingPageChangeCallback)
    }

    private fun updateCircleMarker(binding: ActivityScreenSlideBinding, position: Int) {
        when (position) {
            0 -> {
                binding.onBoardingInitialCircle.background = AppCompatResources.getDrawable(this, R.drawable.bg_blue_circle)
                binding.onBoardingMiddleCircle.background = AppCompatResources.getDrawable(this, R.drawable.bg_gray_circle)
                binding.onBoardingLastCircle.background = AppCompatResources.getDrawable(this, R.drawable.bg_gray_circle)
            }
            1 -> {
                binding.onBoardingInitialCircle.background = AppCompatResources.getDrawable(this, R.drawable.bg_gray_circle)
                binding.onBoardingMiddleCircle.background = AppCompatResources.getDrawable(this, R.drawable.bg_blue_circle)
                binding.onBoardingLastCircle.background = AppCompatResources.getDrawable(this, R.drawable.bg_gray_circle)
            }
            2 -> {
                binding.onBoardingInitialCircle.background = AppCompatResources.getDrawable(this, R.drawable.bg_gray_circle)
                binding.onBoardingMiddleCircle.background = AppCompatResources.getDrawable(this, R.drawable.bg_gray_circle)
                binding.onBoardingLastCircle.background = AppCompatResources.getDrawable(this, R.drawable.bg_blue_circle)
            }
        }
    }

    override fun onDestroy() {
        binding.onBoardingViewPager.unregisterOnPageChangeCallback(onBoardingPageChangeCallback)
        super.onDestroy()
    }
}