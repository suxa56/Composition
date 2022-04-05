package com.suxa.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.suxa.composition.R
import com.suxa.composition.databinding.FragmentGameFinishedBinding
import com.suxa.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        putResult()
        backPressedCallback()
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    private fun putResult() {
        if (gameResult.winner) {
            binding.emojiResult.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_smile
                )
            )
        } else {
            binding.emojiResult.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_sad
                )
            )
        }
        binding.tvRequiredAnswers.text = getString(
            R.string.required_score,
            gameResult.gameSettings.minCountOfRightQuestions.toString()
        )
        binding.tvScoreAnswers.text = getString(
            R.string.score_answers,
            gameResult.countOfRightAnswers.toString()
        )
        binding.tvRequiredPercentage.text = getString(
            R.string.required_percentage,
            gameResult.gameSettings.minPercentOfRightQuestions.toString()
        )
        binding.tvScorePercentage.text = getString(
            R.string.score_percentage, (
                    gameResult.countOfRightAnswers.toDouble()
                            / gameResult.countOfQuestions.toDouble() * 100)
                .toInt().toString()
        )
    }

    private fun backPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            })
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {

        private const val KEY_GAME_RESULT = "GameResult"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}
