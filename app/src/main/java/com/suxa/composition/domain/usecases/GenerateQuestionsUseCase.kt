package com.suxa.composition.domain.usecases

import com.suxa.composition.domain.entity.Question
import com.suxa.composition.domain.repository.Repository

class GenerateQuestionsUseCase(private val repository: Repository) {

    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}