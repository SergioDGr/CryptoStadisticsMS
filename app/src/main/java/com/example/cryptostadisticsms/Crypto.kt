package com.example.cryptostadisticsms

import java.io.Serializable

class Crypto(name: String, precio: Double) : Serializable {

    var name = name
    var precio = precio
}