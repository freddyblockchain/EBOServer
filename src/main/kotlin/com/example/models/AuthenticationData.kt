package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class VerificationData(val message: String, val signature: ByteArray, val address: String)

@Serializable
data class AuthenticationData(
    val verificationData: VerificationData,
    val localPort: Int,
)