package com.example.game.Abilities

import com.badlogic.gdx.math.Vector2
import com.example.game.Algorand.AlgorandManager
import com.example.game.GameObjects.MoveableObjects.Projectile.Fireball
import com.example.game.GameObjects.MoveableObjects.Projectile.Icicle
import com.example.game.GameObjects.MoveableObjects.Projectile.Snowball
import com.mygdx.game.Abilities.Ability

class AbilityManager {
    companion object {
        val fireballAbility = ProjectileAbility(5f, ::Fireball, Vector2(60f,30f))
        val icicleAbility  = ProjectileAbility(3f, ::Icicle, Vector2(50f,17f))
        val snowballAbility = ProjectileAbility(10f, ::Snowball, Vector2(64f,64f))
        val abilityMap = mapOf<Long, Ability>(AlgorandManager.fireballAsa to fireballAbility,
                                             AlgorandManager.icicleAsa to icicleAbility)
    }
}