package com.example.game.Abilities

import Player
import com.badlogic.gdx.math.MathUtils.ceil
import com.badlogic.gdx.math.Vector2
import com.example.game.Networking.Models.FIGHTER_STATE
import com.mygdx.game.Abilities.ABILITY_TYPE
import com.mygdx.game.Abilities.Ability
import distance
import getUnitVectorTowardsPoint

class DashAbility() : Ability(6f) {
    val dashInitialSpeed = 5
    override val abilityType = ABILITY_TYPE.DASH
    override fun onActivate(targetPos: Vector2, player: Player) {
        val playerPos = player.currentPosition()
        val toGo = getUnitVectorTowardsPoint(playerPos,targetPos)

        player.currentUnitVector = toGo
        player.fighterState = FIGHTER_STATE.DASHING
        player.currentSpeed = player.normalSpeed * dashInitialSpeed
    }
}