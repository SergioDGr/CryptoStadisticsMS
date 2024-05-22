package com.example.cryptostadisticsms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptostadisticsms.databinding.ActivityCryptoBinding
import com.example.cryptostadisticsms.elements.Crypto

/**
 * Actividad que muestra una Cryptomoneda
 */
class CryptoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCryptoBinding

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCryptoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val crypto = intent.getSerializableExtra("Moneda") as Crypto
        binding.tvCrypto.text = crypto.name

        //Para volver la actividad anterior
        binding.ivGoBack.setOnClickListener { finish() }
    }

}