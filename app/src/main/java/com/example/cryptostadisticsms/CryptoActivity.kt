package com.example.cryptostadisticsms

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.cryptostadisticsms.databinding.ActivityCryptoBinding
import com.example.cryptostadisticsms.elements.Crypto

/**
 * Actividad que muestra una Cryptomoneda
 */
class CryptoActivity : AppCompatActivity() {

    private val channelID = "channelID"
    private val channelName = "channelName"
    private val notificationId = 0
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var binding: ActivityCryptoBinding

    /**
     * Cuando se crea la vista se define lo que se visualiza cada campo de la crypto y
     * se le define un boton para que notifica si es momento para comprar o vender.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCryptoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val crypto = intent.getSerializableExtra("Moneda") as Crypto
        binding.tvCrypto.text = crypto.name

        //Para volver la actividad anterior
        binding.ivGoBack.setOnClickListener { finish() }

        //Inicializar Python
        if (! Python.isStarted()) {
            Python.start( AndroidPlatform(this));
        }

        val py = Python.getInstance()
        val module = py.getModule("Bot")
        val fact = module["trading_bot"]

        notificationManager = NotificationManagerCompat.from(this)
        createNotificationChannel()
        binding.btnNewNotification.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return@setOnClickListener
            }
            val msg = fact?.call()
            val notification: Notification = NotificationCompat.Builder(this, channelID).also {
                it.setContentTitle("Siguiente movimiento")
                it.setContentText(msg.toString())
                it.setSmallIcon(R.drawable.ic_message)
                it.setPriority(NotificationCompat.PRIORITY_HIGH)
            }.build()
            notificationManager.notify(notificationId, notification)
        }
    }

    /**
     * Crea el canal de notificacion
     */
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelID, channelName, importance).apply {
                lightColor = Color.RED
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }
}