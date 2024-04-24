package com.example.models

import kotlinx.serialization.Serializable
import kotlin.reflect.jvm.internal.impl.incremental.components.Position

@Serializable
data class PlayerState(val position: Pair<Float,Float>)