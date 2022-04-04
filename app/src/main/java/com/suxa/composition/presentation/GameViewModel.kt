package com.suxa.composition.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.suxa.composition.data.GameRepositoryImpl
import com.suxa.composition.domain.entity.GameSettings
import com.suxa.composition.domain.entity.Level
import com.suxa.composition.domain.entity.Question
import com.suxa.composition.domain.usecases.GenerateQuestionsUseCase
import com.suxa.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel : ViewModel() {

    private val repository = GameRepositoryImpl

    private val generateQuestions = GenerateQuestionsUseCase(repository)
    private val getGameSettings = GetGameSettingsUseCase(repository)

    /** Live Data **/
    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() {
            return _question
        }

    private val _settings = MutableLiveData<GameSettings>()
    val settings: LiveData<GameSettings>
        get() {
            return _settings
        }

    /** Funs **/
    fun initSettings(level: Level) {
        _settings.value = getGameSettings.invoke(level)
    }

    fun initQuestion() {
        _settings.value?.let {
            _question.value = generateQuestions.invoke(it.maxSumValue)
        }
    }
}