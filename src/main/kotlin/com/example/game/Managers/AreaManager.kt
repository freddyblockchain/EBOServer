package com.mygdx.game.Managers

import com.mygdx.game.Area.Area
import com.mygdx.game.Collition.CollisionType
import com.mygdx.game.GameObjects.GameObject.GameObject

class AreaManager {
    companion object {
        var currentVersion: Int = 1
        val areas = mutableListOf<Area>()

        val startingAreaName = "World1"

        fun getStartingArea():Area{
            return areas.first{it.areaIdentifier == startingAreaName}
        }

        fun getObjectWithIid(iidToFind: String): GameObject {
            val allObjects = areas.flatMap { it.gameObjects }
            return allObjects.filter { it.gameObjectIid == iidToFind }.first()
        }
    }
}