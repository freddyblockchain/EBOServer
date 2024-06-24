package com.mygdx.game.GameObjects.Hazards

import Player
import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.mygdx.game.Area.Area
import com.mygdx.game.Collisions.DefaultAreaEntranceCollition
import com.mygdx.game.GameObjects.GameObject.GameObject
import com.mygdx.game.StatusEffects.DotEffect

class Lava(gameObjectData: GameObjectData, currentArea: Area)
    : GameObject(gameObjectData, Vector2(gameObjectData.width.toFloat(),gameObjectData.height.toFloat()), currentArea){
    override val collision = LavaCollision()
}
class LavaCollision(): DefaultAreaEntranceCollition() {
    val lavaDOT = DotEffect(0.5f)
    override fun movedInsideAction(objectEntered: GameObject) {
        if(objectEntered is Player){
            objectEntered.statusEffects.add(lavaDOT)
        }
    }

    override fun movedOutsideAction(objectLeaved: GameObject) {
        if(objectLeaved is Player){
            objectLeaved.statusEffects.remove(lavaDOT)
        }
    }

    override var canMoveAfterCollision = true

}