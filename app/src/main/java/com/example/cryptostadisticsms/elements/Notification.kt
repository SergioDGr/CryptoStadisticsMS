package com.example.cryptostadisticsms.elements

/**
 * Clase que representa una notificacion
 */
data class Notification (
    val message: String, //Mensaje
    val image: Int, // La imagen refrenta a que cryptomenda
    val price: Float, //El precio que tiene
    val crypto: String //Que cryptomoneda es
)