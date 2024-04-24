package com.example.game

import initAreas
import initObjects

class GameServerInit {
    companion object {
        fun Init() {
            initMappings()
            initAreas()
            initObjects()
        }
    }
}