package com.mygdx.game.Collition

import Player
import com.mygdx.game.GameObjects.GameObject.GameObject

interface CollisionMask {
    val canCollideWith: (GameObject) -> Boolean
}

class DefaultCollisionMask(override val canCollideWith: (GameObject) -> Boolean = { _: GameObject -> true }):
    CollisionMask {
}

object OnlyPlayerCollitionMask: CollisionMask{
    override val canCollideWith: (GameObject) -> Boolean = { other: GameObject -> other is Player }
}