package com.mygdx.game.Abilities

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Timer.CooldownTimer
import com.mygdx.game.Timer.Timer

abstract class Ability(val cooldown: Float) {
    protected abstract fun onActivate(targetPos: Vector2)
    val timer = CooldownTimer(cooldown)
    fun tryActivate(targetPos: Vector2){
        if(timer.tryUseCooldown()){
            onActivate(targetPos)
        }
    }
}
