package com.example.cryptostadisticsms

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform


class MainActivity : AppCompatActivity() {

    private val CHANNEl_ID = "channelID"
    private val channelName = "channelName"

    private val notificationId = 0

    private var lstCryptos: ArrayList<Crypto>? = ArrayList()

    private lateinit var rlBitcoin: RelativeLayout;
    private lateinit var rlEtherium: RelativeLayout;
    private lateinit var rlsolana: RelativeLayout;

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        if (! Python.isStarted()) {
            Python.start( AndroidPlatform(this));
        }

        val py = Python.getInstance()
        val module = py.getModule("Prueba")

        val fact = module["get_price"]
        fact?.call()

        // En tu fragment o actividad
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isGranted ->
            if(!isGranted){ //El usuario denegó el permiso, muestra un mensaje o toma medidas adecuadas
                finish()
            }
        }

        if(checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        intent = Intent(this@MainActivity, StartupActivity::class.java)
        startActivity(intent)

        val btnNewNotification = findViewById<Button>(R.id.btn_newNotification)
        rlBitcoin = findViewById(R.id.rlBitcoin);

        loadCryptos(py)

        createNotificationChannel()

        val notification: Notification = NotificationCompat.Builder(this, CHANNEl_ID).also {
            it.setContentTitle("Titulo de notifiacion")
            it.setContentText("Este es el contenido de la notificacion")
            it.setSmallIcon(R.drawable.ic_message)
            it.setPriority(NotificationCompat.PRIORITY_HIGH)
        }.build()

        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(this)

        btnNewNotification.setOnClickListener {
            notificationManager.notify(notificationId, notification)
        }

        rlBitcoin.setOnClickListener {

            val bitcoin = lstCryptos?.get(0)

            intent = Intent(this@MainActivity, CryptoActivity::class.java)
            intent.putExtra("Bitcoin", bitcoin)
            startActivity(intent)
        }

    }

    private fun loadCryptos(python: Python) {
        lstCryptos?.add(Crypto("Bitcoin", 1.2))
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEl_ID, channelName, importance).apply {
                lightColor = Color.RED
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }

}