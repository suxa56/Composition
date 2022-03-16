package com.suxa.composition.domain.entity

data class GameSettings(
    val maxSumValue: Int,
    val minCountOfRightQuestions: Int,
    val minPercentOfRightQuestions: Int,
    val gameTimeInSeconds: Int
)
