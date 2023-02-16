package id.ryan.suitgame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.ryan.suitgame.databinding.ActivityLaunchBinding

@SuppressLint("CustomSplashScreen")
class LaunchActivity : AppCompatActivity() {

    private val binding: ActivityLaunchBinding by lazy { ActivityLaunchBinding.inflate(layoutInflater) }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Glide.with(this)
            .load(IMG_URL)
            .into(binding.ivTitleText)

        setContentView(binding.root)
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, ScreenSlideActivity::class.java))
            finish()
        }, 3000)
    }

    companion object {
        const val IMG_URL = "https://i.ibb.co/HC5ZPgD/splash-screen1.png"
    }
}