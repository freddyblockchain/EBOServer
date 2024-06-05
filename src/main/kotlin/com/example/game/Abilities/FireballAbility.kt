package com.example.game.Abilities

import Player
import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.example.game.GameObjects.MoveableObjects.Projectile.Fireball
import com.mygdx.game.Abilities.Ability
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.shootProjectile
import getUnitVectorTowardsPoint

class FireballAbility: Ability(cooldown = 3f) {
    override fun onActivate(targetPos: Vector2, player: Player) {
        val playerPos = player.currentMiddle
        val direction = getUnitVectorTowardsPoint(playerPos,targetPos)
        player.shootProjectile(Fireball(GameObjectData(x = playerPos.x.toInt(), y= playerPos.y.toInt()),  Vector2(60f,30f), direction))
    }
}