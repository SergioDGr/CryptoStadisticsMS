package com.example.cryptostadisticsms

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cryptostadisticsms.databinding.ActivityCryptoBinding
import com.example.cryptostadisticsms.databinding.ActivityMainBinding

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