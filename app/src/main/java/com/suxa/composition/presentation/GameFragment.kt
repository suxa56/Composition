package com.suxa.composition.presentation

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.suxa.composition.R
import com.suxa.composition.databinding.FragmentGameBinding
import com.suxa.composition.domain.entity.GameResult
import com.suxa.composition.domain.entity.Level

class GameFragment : Fragment() {

    private lateinit var level: Level

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GameViewModel

    var countOfRightAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingUpViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Util Funs **/
    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    private fun settingUpViewModel() {
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        viewModel.initQuestionsAndSettings(level)

        viewModel.question.observe(viewLifecycleOwner) {
            with(binding) {
                tvSum.text = it.sum.toString()
                tvLeftNumber.text = it.visibleNumber.toString()
                tvOption1.text = it.options[0].toString()
                tvOption2.text = it.options[1].toString()
                tvOption3.text = it.options[2].toString()
                tvOption4.text = it.options[3].toString()
                tvOption5.text = it.options[4].toString()
                tvOption6.text = it.options[5].toString()
            }
        }

        viewModel.settings.observe(viewLifecycleOwner) {
            binding.tvAnswersProgress.text = getString(
                R.string.progress_answers,
                countOfRightAnswers.toString(),
                it.minCountOfRightQuestions.toString()
            )
            launchTimer(it.gameTimeInMilliseconds)
        }
    }

    private fun launchTimer(timeInMilliseconds: Long) {
        var countDown = timeInMilliseconds
        object : CountDownTimer(countDown, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                countDown = millisUntilFinished
                val minutes: Int = (millisUntilFinished / 60000).toInt()
                val seconds: Int = (millisUntilFinished % 60000 / 1000).toInt()
                val secondsLeft = if (seconds < 10) {
                    "0".plus(seconds.toString())
                } else {
                    seconds.toString()
                }
                binding.tvTimer.text = getString(R.string.timer, minutes.toString(), secondsLeft)
            }

            override fun onFinish() {
//                launchGameFinishedFragment()
            }
        }.start()
    }

    fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    companion object {

        const val NAME = "GameFragment"
        private const val KEY_LEVEL = "level"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }

            }
        }
    }
}
