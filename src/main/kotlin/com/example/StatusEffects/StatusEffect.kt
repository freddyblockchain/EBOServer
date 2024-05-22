package com.mygdx.game.StatusEffects

import com.example.game.GameObjects.GameObject.FightableEntity


interface StatusEffect {
    fun triggerEffect(fightableEntity: FightableEntity)
}

class DotEffect(val healthDecreasePerTick: Float): StatusEffect {

    override fun triggerEffect(fightableEntity: FightableEntity) {
        fightableEntity.health -= healthDecreasePerTick
    }
}