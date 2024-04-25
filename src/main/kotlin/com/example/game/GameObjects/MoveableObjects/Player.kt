

import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.mygdx.game.Abilities.Ability
import com.mygdx.game.CannotMoveStrategy.NoAction
import com.mygdx.game.Collisions.CanMoveCollision
import com.mygdx.game.Enums.Direction
import com.mygdx.game.Enums.PlayerState
import com.mygdx.game.GameObjects.GameObject.MoveableObject

enum class PLAYER_STATUS {ALIVE, DEAD}
class Player(gameObjectData: GameObjectData, size: Vector2, val sessionKey: String)
    : MoveableObject(gameObjectData, size) {
    override var speed: Float = 2f
    override val cannotMoveStrategy = NoAction()
    override var direction = Direction.RIGHT
    override var canChangeDirection = true
    override val collision = CanMoveCollision()
    val abilities: MutableList<Ability> = mutableListOf()
    var state: PlayerState = PlayerState.NORMAL
}