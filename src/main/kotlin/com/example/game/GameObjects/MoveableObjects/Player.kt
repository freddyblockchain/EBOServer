

import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.example.game.GameObjects.GameObject.FightableEntity
import com.example.game.Networking.Models.*
import com.mygdx.game.Abilities.Ability
import com.mygdx.game.CannotMoveStrategy.NoAction
import com.mygdx.game.Collisions.CanMoveCollision
import com.mygdx.game.Enums.Direction
import com.mygdx.game.GameObjects.GameObject.MoveableObject
import com.mygdx.game.StatusEffects.StatusEffect

enum class PLAYER_STATUS {ALIVE, DEAD}

class Player(gameObjectData: GameObjectData, size: Vector2, val playerNum: Int)
    : MoveableObject(gameObjectData, size), ServerGameObjectConverter, FightableEntity {

    override var speed: Float = 4f
    override val cannotMoveStrategy = NoAction()
    override var direction = Direction.RIGHT
    override var canChangeDirection = true
    override val collision = CanMoveCollision()
    var movementFrames: Int = 0
    val abilities: MutableList<Ability> = mutableListOf()
    var status: PLAYER_STATUS = PLAYER_STATUS.ALIVE

    override var health = 100f
    override val maxHealth = 100f
    var statusEffects = mutableListOf<StatusEffect>()

    override fun frameTask() {
        if(movementFrames > 0){
            this.move(this.currentUnitVector)
            movementFrames -= 1
        }
        super.frameTask()
        statusEffects.forEach{ it.triggerEffect(this) }
    }

    override fun converToServerGameObject(): ServerGameObject {
        return ServerGameObject(DefaultMoveableObjectData(this.playerNum, GameObjectType.PLAYER), CustomFields.PlayerCustomFields(status, health))
    }
}