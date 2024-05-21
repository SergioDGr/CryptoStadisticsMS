package com.example.cryptostadisticsms

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable

import android.os.Build
import android.os.Bundle
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

import com.example.cryptostadisticsms.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val channelID = "channelID"
    private val channelName = "channelName"

    private var lstCryptoItems: ArrayList<CryptoItem> = ArrayList()

    private lateinit var adapter: CryptoAdapter
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingPermission", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView =  binding.navigationView
        val drawView = binding.dlMain

        val navBar = binding.ivNavBar

        navBar.setOnClickListener {
            drawView.open()
        }

        lstCryptoItems.add(CryptoItem("Bitcoin", R.drawable.bitcoin, R.drawable.card_1))
        lstCryptoItems.add(CryptoItem("Ethereum", R.drawable.ethereum, R.drawable.card_2))
        lstCryptoItems.add(CryptoItem("Solana", R.drawable.solana, R.drawable.card_3))

        adapter = CryptoAdapter(this, lstCryptoItems)

        binding.gvCryptos.adapter = adapter
        binding.gvCryptos.setOnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position) as CryptoItem
            intent = Intent(this@MainActivity, CryptoActivity::class.java)
            intent.putExtra("Bitcoin", item)
            startActivity(intent)
        }

        binding.gvCryptos.setOnItemLongClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position) as CryptoItem
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.del_really_crypto))
                .setTitle(getString(R.string.del_crypto))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    lstCryptoItems.remove(item)
                    adapter.notifyDataSetChanged()
                }
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
            return@setOnItemLongClickListener true
        }


        if (! Python.isStarted()) {
            Python.start( AndroidPlatform(this));
        }

        val py = Python.getInstance()
        val module = py.getModule("Prueba")

        /*val fact = module["trading_bot"]
        fact?.call()*/

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

        //val btnNewNotification = findViewById<Button>(R.id.btn_newNotification)
        //rlBitcoin = findViewById(R.id.rlBitcoin);

        //loadCryptos(py)
        createNotificationChannel()

        val notification: Notification = NotificationCompat.Builder(this, channelID).also {
            it.setContentTitle("Titulo de notifiacion")
            it.setContentText("Este es el contenido de la notificación")
            it.setSmallIcon(R.drawable.ic_message)
            it.setPriority(NotificationCompat.PRIORITY_HIGH)
        }.build()

        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(this)

        /*btnNewNotification.setOnClickListener {
            notificationManager.notify(notificationId, notification)
        }*/

        /*rlBitcoin.setOnClickListener {

            val bitcoin = lstCryptos?.get(0)

            intent = Intent(this@MainActivity, CryptoActivity::class.java)
            intent.putExtra("Bitcoin", bitcoin)
            startActivity(intent)
        }*/

        //El evento para añadir una moneda
        binding.ivAddCrypro.setOnClickListener{
            val strSearch =binding.etBuscar.text.toString()
            if(strSearch.isEmpty()) { //Si esta vacio en el buscador
                Toast.makeText(this, "No se ha pasado ninguna moneda",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(searchCrypto(strSearch)) { //Si existe la moneda
                lstCryptoItems.add( CryptoItem(strSearch, 0, generateImage()))
                adapter.notifyDataSetChanged()
                binding.etBuscar.text.clear()
            }else{ //en caso contrario
                Toast.makeText(this, "No se ha encontrado la cryptomoneda",
                    Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun generateImage(): Int{
        val num = (1..3).random()
        return when(num){
            1 -> R.drawable.card_1
            2 -> R.drawable.card_2
            else -> R.drawable.card_3
        }
    }

    private fun loadCryptos(python: Python) {
        //lstCryptos?.add(Crypto("Bitcoin", 1.2))
    }

    private fun searchCrypto(str: String): Boolean{
        return str.lowercase() == "dogcoin";
    }

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