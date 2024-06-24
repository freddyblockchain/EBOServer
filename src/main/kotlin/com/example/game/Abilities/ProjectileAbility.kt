package com.example.game.Abilities

import Player
import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.mygdx.game.Abilities.ABILITY_TYPE
import com.mygdx.game.Abilities.Ability
import com.mygdx.game.Area.Area
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.Projectile
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.shootProjectile
import getUnitVectorTowardsPoint
class ProjectileAbility(cooldown: Float, val projectileCreator: (gameObjectData: GameObjectData, size: Vector2, unitVectorDirection: Vector2, currentArea: Area) -> Projectile, val size: Vector2, override val abilityType: ABILITY_TYPE): Ability(cooldown = cooldown){

    override fun onActivate(targetPos: Vector2, player: Player) {
        val playerPos = player.currentMiddle
        val direction = getUnitVectorTowardsPoint(playerPos,targetPos)
        player.shootProjectile(projectileCreator(GameObjectData(x = playerPos.x.toInt(), y= playerPos.y.toInt()), size ,direction, player.currentArea))
    }
}