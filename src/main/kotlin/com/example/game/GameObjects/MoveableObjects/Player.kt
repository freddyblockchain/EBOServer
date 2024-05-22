import com.badlogic.gdx.math.Vector2
import com.example.game.GameObjectData
import com.example.game.GameObjects.GameObject.FIGHTER_STATE
import com.example.game.GameObjects.GameObject.FightableEntity
import com.example.game.GameObjects.GameObject.FightableObject
import com.example.game.Networking.GameServerInit
import com.example.game.Networking.Models.*
import com.mygdx.game.Abilities.Ability
import com.mygdx.game.CannotMoveStrategy.NoAction
import com.mygdx.game.Collisions.AreaEntranceCollition
import com.mygdx.game.Collisions.CanMoveCollision
import com.mygdx.game.Enums.Direction
import com.mygdx.game.GameObjects.GameObject.MoveableObject
import com.mygdx.game.Managers.AreaManager
import com.mygdx.game.StatusEffects.StatusEffect

enum class PLAYER_STATUS { ALIVE, DEAD }

class Player(gameObjectData: GameObjectData, size: Vector2, val playerNum: Int) : FightableObject(gameObjectData, size),
    ServerGameObjectConverter {

    override var normalSpeed = 4f
    override var currentSpeed = normalSpeed
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
        if (movementFrames > 0 && this.fighterState == FIGHTER_STATE.FREE) {
            this.move(this.currentUnitVector)
            movementFrames -= 1
        }
        statusEffects.forEach { it.triggerEffect(this) }
        super.frameTask()
    }

    override fun converToServerGameObject(): ServerGameObject {
        return ServerGameObject(
            DefaultMoveableObjectData(this.playerNum, GameObjectType.PLAYER),
            CustomFields.PlayerCustomFields(status, health)
        )
    }

    override fun death() {
        this.setPosition(GameServerInit.playerInitPosition)
        this.health = maxHealth
        this.fighterState = FIGHTER_STATE.FREE
        this.launchUnitVector = Vector2(0f, 0f)
        this.movementFrames = 0
        this.status = PLAYER_STATUS.ALIVE
        this.currentUnitVector = Vector2(0f, 0f)
        this.currentSpeed = this.normalSpeed

        val areaEnteredCollisions: List<AreaEntranceCollition> =
            AreaManager.getActiveArea()!!.gameObjects.map { it.collision }.filterIsInstance<AreaEntranceCollition>()

        areaEnteredCollisions.forEach {
            it.movedOutside(this)
        }
    }
}