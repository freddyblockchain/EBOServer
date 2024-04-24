package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthorizationData(
    val signedMessage: String, // The digital signature
    val roundNrSigned: Long,   // The blockchain round number
    val algorandAddress: String, // The Algorand address of the signer
    val roundSeed: String     // The message that was signed, i.e., the round seed
)