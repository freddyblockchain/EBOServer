

import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.mygdx.game.Abilities.Ability
import com.mygdx.game.CannotMoveStrategy.NoAction
import com.mygdx.game.Collisions.CanMoveCollision
import com.mygdx.game.Enums.Direction
import com.mygdx.game.Enums.PlayerState
import com.mygdx.game.GameObjects.GameObject.MoveableObject

enum class PLAYER_STATUS {ALIVE, DEAD}

data class MovementIntent(val unitVector: Vector2, val movementFrames: Int)
class Player(gameObjectData: GameObjectData, size: Vector2, val playerNum: Int)
    : MoveableObject(gameObjectData, size) {

    override var speed: Float = 20f
    override val cannotMoveStrategy = NoAction()
    override var direction = Direction.RIGHT
    override var canChangeDirection = true
    override val collision = CanMoveCollision()
    var movementFrames: Int = 0
    val abilities: MutableList<Ability> = mutableListOf()
    var state: PLAYER_STATUS = PLAYER_STATUS.ALIVE

    override fun frameTask() {
        if(movementFrames > 0){
            this.move(this.currentUnitVector)
            movementFrames -= 1
        } else{
            currentUnitVector = Vector2(0f,0f)
        }
        super.frameTask()
    }
}