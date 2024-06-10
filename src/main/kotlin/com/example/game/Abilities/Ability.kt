package com.mygdx.game.Abilities

import Player
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Timer.CooldownTimer
import com.mygdx.game.Timer.Timer

enum class ABILITY_TYPE {FIREBALL, ICICLE, SNOWBALL, DASH}

abstract class Ability(val cooldown: Float) {
    abstract val abilityType: ABILITY_TYPE
    protected abstract fun onActivate(targetPos: Vector2, player: Player)
    val timer = CooldownTimer(cooldown)
    fun tryActivate(targetPos: Vector2, player: Player){
        if(timer.tryUseCooldown()){
            onActivate(targetPos, player)
        }
    }
}
