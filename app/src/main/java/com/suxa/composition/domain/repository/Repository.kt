package com.suxa.composition.domain.repository

import com.suxa.composition.domain.entity.GameSettings
import com.suxa.composition.domain.entity.Level
import com.suxa.composition.domain.entity.Question

interface Repository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int,
    ): Question

    fun getGameSettings(level: Level): GameSettings
}