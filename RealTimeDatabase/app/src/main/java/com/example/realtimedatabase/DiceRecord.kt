package com.example.realtimedatabase

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DiceRecord(
    val username: String? = null,
    val dice1: String? = null,
    val dice2: String? = null,
    val dice3: String? = null,
    val dice4: String? = null,
    val timestamp: String? = null)
