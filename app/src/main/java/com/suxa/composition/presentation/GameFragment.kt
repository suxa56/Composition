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
import com.suxa.composition.domain.entity.GameSettings
import com.suxa.composition.domain.entity.Level

class GameFragment : Fragment() {

    private lateinit var level: Level

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GameViewModel

    private var rightAnswersCount = 0
    private var totalAnswersCount = 0
    private var minAnswersCount = 0
    private var totalQuestionsCount = 0
    private var sum = 0
    private var visibleNumber = 0
    private var minPercent = 0


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
        configureProgressBar()
        optionsClickListener()
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

        viewModel.initSettings(level)
        viewModel.initQuestion()

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
            sum = it.sum
            visibleNumber = it.visibleNumber
        }

        viewModel.settings.observe(viewLifecycleOwner) {
            minAnswersCount = it.minCountOfRightQuestions
            minPercent = it.minPercentOfRightQuestions
            totalQuestionsCount = 100 / it.minPercentOfRightQuestions * it.minCountOfRightQuestions
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
                launchGameFinishedFragment(calculateGameResult())
            }
        }.start()
    }

    private fun optionsClickListener() {
        binding.tvOption1.setOnClickListener {
            checkAnswer(binding.tvOption1.text.toString().toInt())
        }
        binding.tvOption2.setOnClickListener {
            checkAnswer(binding.tvOption2.text.toString().toInt())
        }
        binding.tvOption3.setOnClickListener {
            checkAnswer(binding.tvOption3.text.toString().toInt())
        }
        binding.tvOption4.setOnClickListener {
            checkAnswer(binding.tvOption4.text.toString().toInt())
        }
        binding.tvOption5.setOnClickListener {
            checkAnswer(binding.tvOption5.text.toString().toInt())
        }
        binding.tvOption6.setOnClickListener {
            checkAnswer(binding.tvOption6.text.toString().toInt())
        }
    }

    private fun checkAnswer(optionText: Int) {
        if ((sum - visibleNumber) == optionText) {
            rightAnswer()
        } else {
            wrongAnswer()
        }

        configureProgressBar()
        viewModel.initQuestion()
    }

    private fun rightAnswer() {
        rightAnswersCount += 1
        totalAnswersCount += 1
    }

    private fun wrongAnswer() {
        totalAnswersCount += 1
    }

    private fun configureProgressBar() {
        val progressBar = binding.progressBar
        if (totalAnswersCount > totalQuestionsCount) {
            totalQuestionsCount += 1
        }
        progressBar.max = totalQuestionsCount
        progressBar.progress = rightAnswersCount
        binding.tvAnswersProgress.text = getString(
            R.string.progress_answers,
            rightAnswersCount.toString(),
            minAnswersCount.toString()
        )
    }

    private fun calculateGameResult():GameResult {
        val gameSettings: GameSettings = viewModel.settings.value!!
        return GameResult(isWinner(), rightAnswersCount, totalQuestionsCount, gameSettings)
    }

    private fun isWinner(): Boolean {
        return rightAnswersCount >= minAnswersCount &&
                (rightAnswersCount/totalQuestionsCount)*100 >= minPercent
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
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
