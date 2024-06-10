package com.example.game.Abilities

import com.badlogic.gdx.math.Vector2
import com.example.game.Algorand.AlgorandManager
import com.example.game.GameObjects.MoveableObjects.Projectile.Fireball
import com.example.game.GameObjects.MoveableObjects.Projectile.Icicle
import com.example.game.GameObjects.MoveableObjects.Projectile.Snowball
import com.mygdx.game.Abilities.ABILITY_TYPE
import com.mygdx.game.Abilities.Ability

class AbilityManager {
    companion object {
        val abilityCreatorMap = mapOf<Long, () -> Ability>(
            AlgorandManager.fireballAsa to { ProjectileAbility(3f, ::Fireball, Vector2(60f, 30f), ABILITY_TYPE.FIREBALL) },
            AlgorandManager.icicleAsa to { ProjectileAbility(3f, ::Icicle, Vector2(50f, 17f), ABILITY_TYPE.ICICLE) },
            AlgorandManager.snowballAsa to { ProjectileAbility(6f, ::Snowball, Vector2(64f, 64f), ABILITY_TYPE.SNOWBALL) },
            AlgorandManager.dashAsa to { DashAbility() }
        )
    }
}