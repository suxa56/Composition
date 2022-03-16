package com.suxa.composition.domain.usecases

import com.suxa.composition.domain.entity.GameSettings
import com.suxa.composition.domain.entity.Level
import com.suxa.composition.domain.repository.Repository

class GetGameSettingsUseCase(private val repository: Repository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}