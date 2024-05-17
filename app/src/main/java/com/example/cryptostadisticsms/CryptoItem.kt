package com.example.cryptostadisticsms

import java.io.Serializable

data class CryptoItem(
    val name: String,
    val image: Int,
    val background: Int
): Serializable
