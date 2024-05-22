package com.example.cryptostadisticsms.elements

import java.io.Serializable

/**
 * Clase que representa una cryptomoneda
 */
data class Crypto(
    val name: String, //Nombre de la cryptomenda
    val image: Int, //Imagen de la cryptomoneda
    val background: Int //Un fondo para ella
): Serializable
