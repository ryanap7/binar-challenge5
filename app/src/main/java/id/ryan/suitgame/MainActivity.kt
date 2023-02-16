package id.ryan.suitgame

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import id.ryan.suitgame.domain.GameMode
import id.ryan.suitgame.domain.GameState
import id.ryan.suitgame.domain.UserPrefConstant
import id.ryan.suitgame.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import id.ryan.suitgame.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences

    private lateinit var mode: GameMode

    private lateinit var binding: ActivityMainBinding

    private lateinit var rockAnimation: AnimationDrawable

    private lateinit var scissorsAnimation: AnimationDrawable

    private lateinit var paperAnimation: AnimationDrawable

    private var _dialog: ResultDialog.Builder? = null

    private val dialog get() = _dialog!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(
            UserPrefConstant.SHARED_PREF_KEY,
            Context.MODE_PRIVATE,
        )

        val username = pref.getString(UserPrefConstant.USERNAME_KEY, "Pemain 1")
        val gameMode = pref.getString(UserPrefConstant.GAMEMODE_KEY, "VERSUS_COM").toString()

        mode = GameMode.valueOf(gameMode)
        binding.tvP1Title.text = username
        binding.tvP2Title.text = if (mode == GameMode.VERSUS_COM) "CPU" else "Pemain 2"

        val viewModel: MainViewModel by viewModels()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { it ->
                    if (it.state == GameState.START) {
                        binding.tvPlayerChosen.visibility = View.INVISIBLE
                        binding.playerOption.batu.setBackgroundColor(Color.TRANSPARENT)
                        binding.playerOption.kertas.setBackgroundColor(Color.TRANSPARENT)
                        binding.playerOption.gunting.setBackgroundColor(Color.TRANSPARENT)

                        binding.computerOption.batu.apply {
                            setBackgroundResource(R.drawable.random_batu_selected)
                            rockAnimation = background as AnimationDrawable
                        }
                        binding.computerOption.kertas.apply {
                            setBackgroundResource(R.drawable.random_kertas_selected)
                            paperAnimation = background as AnimationDrawable
                        }
                        binding.computerOption.gunting.apply {
                            setBackgroundResource(R.drawable.random_gunting_selected)
                            scissorsAnimation = background as AnimationDrawable
                        }

                    }

                    if (it.state == GameState.FINISH) {
                        stopAnimation()
                        val comChosen = it.chosen["computer"].toString().replaceFirstChar {
                            it.uppercase()
                        }
                        val player2 = if (mode == GameMode.VERSUS_COM) "CPU" else "Pemain 2"
                        val comChosenText = resources.getString(R.string.com_chosen_tv, player2, comChosen)
                        binding.tvPlayerChosen.visibility = View.VISIBLE
                        binding.tvPlayerChosen.text = comChosenText
                        when (it.result) {
                            "player" -> {
                                val text = "${username ?: "Pemain 1"} \n MENANG!"
                                dialog.result(text).build().show()
                            }
                            "computer" -> {
                                val text = "$player2 \n MENANG!"
                                dialog.result(text).build().show()
                            }
                            "draw" -> {
                                val text = "DRAW!"
                                dialog.result(text).build().show()
                            }
                        }

                        when (it.chosen["computer"]) {
                            "batu" -> binding.computerOption.batu.setBackgroundResource(R.drawable.rounded_corner)
                            "kertas" -> binding.computerOption.kertas.setBackgroundResource(R.drawable.rounded_corner)
                            "gunting" -> binding.computerOption.gunting.setBackgroundResource(R.drawable.rounded_corner)
                        }
                    }
                }
            }
        }
        _dialog = ResultDialog.Builder()
            .ctx(this)
            .result("DRAW!")
            .callback(object : ResultDialog.OnDialogPressed {
                val activity = this@MainActivity
                override fun onPlayAgain() {
                    viewModel.restartGame()
                }

                override fun onBackToMainMenu() {
                    activity.finish()
                }
            })

        binding.playerOption.batu.setOnClickListener {
            Log.d(TAG, "Player 1 memilih Batu")
            if (viewModel.uiState.value.chosen["player"]!!.isNotBlank()) {
                showSnackBar(it); return@setOnClickListener
            }
            viewModel.startGame(mode, "batu", "")
            if (mode == GameMode.VERSUS_COM) startAnimation()
            it.setBackgroundResource(R.drawable.rounded_corner)
        }

        binding.playerOption.gunting.setOnClickListener {
            Log.d(TAG, "Player 1 memilih Gunting")
            if (viewModel.uiState.value.chosen["player"]!!.isNotBlank()) {
                showSnackBar(it); return@setOnClickListener
            }
            viewModel.startGame(mode, "gunting", "")
            if (mode == GameMode.VERSUS_COM) startAnimation()
            it.setBackgroundResource(R.drawable.rounded_corner)

        }

        binding.playerOption.kertas.setOnClickListener {
            Log.d(TAG, "Player 1 memilih Kertas")
            if (viewModel.uiState.value.chosen["player"]!!.isNotBlank()) {
                showSnackBar(it); return@setOnClickListener
            }
            viewModel.startGame(mode, "kertas", "")
            if (mode == GameMode.VERSUS_COM) startAnimation()
            it.setBackgroundResource(R.drawable.rounded_corner)
        }

        binding.computerOption.batu.setOnClickListener {
            Log.d(TAG, "Player 2 memilih Batu")
            if (mode == GameMode.VERSUS_COM) return@setOnClickListener
            if (viewModel.uiState.value.chosen["computer"]!!.isNotBlank()) {
                showSnackBar(it); return@setOnClickListener
            }
            viewModel.startGame(mode, "", "batu")
            it.setBackgroundResource(R.drawable.rounded_corner)
        }

        binding.computerOption.gunting.setOnClickListener {
            Log.d(TAG, "Player 2 memilih Gunting")
            if (mode == GameMode.VERSUS_COM) return@setOnClickListener
            if (viewModel.uiState.value.chosen["computer"]!!.isNotBlank()) {
                showSnackBar(it); return@setOnClickListener
            }
            viewModel.startGame(mode, "", "gunting")
            it.setBackgroundResource(R.drawable.rounded_corner)

        }

        binding.computerOption.kertas.setOnClickListener {
            Log.d(TAG, "Player 2 memilih Kertas")
            if (mode == GameMode.VERSUS_COM) return@setOnClickListener
            if (viewModel.uiState.value.chosen["computer"]!!.isNotBlank()) {
                showSnackBar(it); return@setOnClickListener
            }
            viewModel.startGame(mode, "", "kertas")
            it.setBackgroundResource(R.drawable.rounded_corner)
        }

        binding.ibRefresh.setOnClickListener {
            viewModel.restartGame()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _dialog = null
    }

    /**
     * Function to start animation randomly
     * selected item
     */
    private fun startAnimation() {
        rockAnimation.start()
        scissorsAnimation.start()
        paperAnimation.start()
    }

    /**
     * Function to stop animation randomly
     * selected item
     */
    private fun stopAnimation() {
        rockAnimation.stop()
        scissorsAnimation.stop()
        paperAnimation.stop()
    }

    /**
     * Function to show a Snackbar with message "Anda sudah memilih restart terlebih dahulu"
     * @param it the view where the Snackbar will be displayed
     */
    private fun showSnackBar(it: View) {
        Snackbar.make(
            it,
            "Anda sudah memilih restart terlebih dahulu",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        val TAG = MainActivity::class.simpleName
    }
}