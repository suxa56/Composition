package com.suxa.composition.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val maxSumValue: Int,
    val minCountOfRightQuestions: Int,
    val minPercentOfRightQuestions: Int,
    val gameTimeInMilliseconds: Long
) : Parcelable
