package id.ryan.suitgame

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog


class ResultDialog private constructor(c: Context, s: String, callback: OnDialogPressed) : AlertDialog(c), View.OnClickListener {

    private lateinit var playAgainButton: Button
    private lateinit var backToMainMenu: Button
    private lateinit var tvResult: TextView

    interface OnDialogPressed {
        fun onPlayAgain()
        fun onBackToMainMenu()
    }

    data class Builder(
        var ctx: Context? = null,
        var callback: OnDialogPressed? = null,
        var result: String? = null
    ) {
        fun ctx(ctx: Context) = apply { this.ctx = ctx }
        fun callback(callback: OnDialogPressed) = apply { this.callback = callback }
        fun result(result: String) = apply { this.result = result }
        fun build() = ResultDialog(ctx!!, result!!, callback!!)
    }

    private val cb: OnDialogPressed = callback
    private val resultText: String = s

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_dialog)
        playAgainButton = findViewById<View>(R.id.btn_play_again) as Button
        backToMainMenu = findViewById<View>(R.id.btn_back_main_menu) as Button
        tvResult = findViewById<View>(R.id.tv_result_big_text) as TextView
        tvResult.text = resultText
        playAgainButton.setOnClickListener(this)
        backToMainMenu.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_play_again -> cb.onPlayAgain()
            R.id.btn_back_main_menu -> cb.onBackToMainMenu()
            else -> {}
        }

        dismiss()
    }
}