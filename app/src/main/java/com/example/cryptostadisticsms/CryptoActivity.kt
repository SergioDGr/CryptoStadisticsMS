package com.example.cryptostadisticsms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptostadisticsms.databinding.ActivityCryptoBinding

class CryptoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCryptoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCryptoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val crypto = intent.getSerializableExtra("Bitcoin") as CryptoItem
        binding.tvCrypto.text = crypto.name

    }


}