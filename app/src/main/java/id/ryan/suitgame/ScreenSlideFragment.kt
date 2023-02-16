package id.ryan.suitgame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import id.ryan.suitgame.domain.UserPrefConstant
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import id.ryan.suitgame.databinding.FragmentScreenSlideBinding


class ScreenSlideFragment : Fragment() {
    private lateinit var pref: SharedPreferences

    private lateinit var binding: FragmentScreenSlideBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScreenSlideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = requireArguments().getInt(ARG_POSITION)
        val onBoardingTitles = requireContext().resources.getStringArray(R.array.on_boarding_titles)
        val onBoardingImages = getOnBoardAssetsLocation()
        pref = activity?.getSharedPreferences(
            UserPrefConstant.SHARED_PREF_KEY,
            Context.MODE_PRIVATE,
        )!!

        with(binding) {
            onBoardingImage.setImageResource(onBoardingImages[position])
            onBoardingTitle.text = onBoardingTitles[position]
            edtTextName.visibility = if (position == 2) View.VISIBLE else View.GONE
            edtTextName.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_NULL && event.action == KeyEvent.ACTION_DOWN) {
                    if (event.keyCode == KeyEvent.KEYCODE_ENTER) onSubmitCallback(v, edtTextName)
                }

                return@setOnEditorActionListener true
            }

            imgBtnSubmit.visibility = if (position == 2) View.VISIBLE else View.GONE
            imgBtnSubmit.setOnClickListener {
                onSubmitCallback(it, edtTextName)
            }
        }
    }

    private fun onSubmitCallback(it: View, edtTextName: TextInputEditText) {
        println("ON SUBMIT CALLBACK")
        var username = edtTextName.text.toString()
            .replaceFirstChar { it.uppercase() }
        if (username.split(" ").size > 1) username = username.first().toString()

        Log.d(TAG, "User fill name with: $username}")
        val editor = pref.edit()
        editor.putString(UserPrefConstant.USERNAME_KEY, username)
        editor.apply()

        if (username.isBlank()) {
            Snackbar.make(
                it,
                "Nama harus diisi terlebih dahulu",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        if (username.isNotBlank()){
            startActivity(Intent(activity, GameModeActivity::class.java))
        }
    }

    private fun getOnBoardAssetsLocation(): List<Int> {
        val onBoardAssets: MutableList<Int> = ArrayList()
        onBoardAssets.add(R.drawable.landing_page1)
        onBoardAssets.add(R.drawable.landing_page2)
        onBoardAssets.add(R.drawable.landing_page3)
        return onBoardAssets
    }

    companion object {
        val TAG = ScreenSlideActivity::class.simpleName

        private const val ARG_POSITION = "ARG_POSITION"

        fun getInstance(position: Int) = ScreenSlideFragment().apply {
            arguments = bundleOf(ARG_POSITION to position)
        }
    }
}