import com.badlogic.gdx.math.Vector2
import com.example.game.Algorand.AlgorandManager
import com.example.game.GameObjectData
import com.example.game.GameObjects.GameObject.FightableObject
import com.example.game.Networking.GameServerInit
import com.example.game.Networking.Models.*
import com.example.game.playerEvents
import com.mygdx.game.Abilities.Ability
import com.mygdx.game.CannotMoveStrategy.NoAction
import com.mygdx.game.Collisions.AreaEntranceCollition
import com.mygdx.game.Collisions.CanMoveCollision
import com.mygdx.game.Enums.Direction
import com.mygdx.game.Managers.AreaManager
import com.mygdx.game.StatusEffects.StatusEffect

enum class PLAYER_STATE { ALIVE, DEAD }

class Player(gameObjectData: GameObjectData, size: Vector2, val playerNum: Int) : FightableObject(gameObjectData, size),
    ServerGameObjectConverter {

    override var normalSpeed = 4f
    override var currentSpeed = normalSpeed
    override val cannotMoveStrategy = NoAction()
    override var direction = Direction.RIGHT
    override var canChangeDirection = true
    override val collision = CanMoveCollision()
    val dashDecreaseDecrement = 1.5f
    var address = ""
    var movementFrames: Int = 0
    val assets: MutableList<Long> = mutableListOf()
    val abilities: MutableList<Ability> = mutableListOf()
    var status: PLAYER_STATE = PLAYER_STATE.ALIVE
    var lastAttacker: Player? = null
    var shouldUpdateGold = false

    override var health = 100f
    override val maxHealth = 100f
    var statusEffects = mutableListOf<StatusEffect>()

    override fun frameTask() {
        if (movementFrames > 0 && this.fighterState == FIGHTER_STATE.FREE) {
            this.move(this.currentUnitVector)
            movementFrames -= 1
        }
        if(this.fighterState == FIGHTER_STATE.DASHING){
            if(this.currentSpeed >= normalSpeed){
                this.currentSpeed -= dashDecreaseDecrement
            } else {
                this.currentSpeed = normalSpeed
                this.fighterState = FIGHTER_STATE.FREE
            }
            this.move(this.currentUnitVector)
        }
        statusEffects.forEach { it.triggerEffect(this) }
        super.frameTask()
    }

    override fun converToServerGameObject(): ServerGameObject {
        return ServerGameObject(
            DefaultMoveableObjectData(this.playerNum, GameObjectType.PLAYER),
            CustomFields.PlayerCustomFields(fighterState, health)
        )
    }

    override fun death() {
        this.setPosition(GameServerInit.playerInitPosition)
        this.health = maxHealth
        this.fighterState = FIGHTER_STATE.FREE
        this.launchUnitVector = Vector2(0f, 0f)
        this.movementFrames = 0
        this.status = PLAYER_STATE.ALIVE
        this.currentUnitVector = Vector2(0f, 0f)
        this.currentSpeed = this.normalSpeed

        val areaEnteredCollisions: List<AreaEntranceCollition> =
            AreaManager.getActiveArea()!!.gameObjects.map { it.collision }.filterIsInstance<AreaEntranceCollition>()

        areaEnteredCollisions.forEach {
            it.movedOutside(this)
        }

        if(this.lastAttacker != null){
            AlgorandManager.sendGold(this.lastAttacker!!.address, 5)
            playerEvents.add(PlayerEvent.PlayerDeath(this.playerNum, this.lastAttacker!!.playerNum, System.currentTimeMillis()))
        }
    }
}