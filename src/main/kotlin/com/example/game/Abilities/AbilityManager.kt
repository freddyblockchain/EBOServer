package com.example.game.Abilities

import com.example.game.Algorand.AlgorandManager
import com.mygdx.game.Abilities.Ability

class AbilityManager {
    companion object {
        val fireballAbility = FireballAbility()
        val abilityMap = mapOf<Long, Ability>(AlgorandManager.fireballAsa to fireballAbility)
    }
}